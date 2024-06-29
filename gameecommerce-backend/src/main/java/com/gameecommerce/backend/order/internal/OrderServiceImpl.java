package com.gameecommerce.backend.order.internal;

import com.gameecommerce.backend.gateway.impl.mercadopago.MercadoPagoGatewayService;
import com.gameecommerce.backend.order.*;
import com.gameecommerce.backend.order.exception.InvalidOrderProductAmountException;
import com.gameecommerce.backend.order.exception.OrderNotFoundException;
import com.gameecommerce.backend.product.ProductRepository;
import com.gameecommerce.backend.product.exception.ProductNotFoundException;
import com.gameecommerce.backend.utils.RandomUtils;
import jakarta.validation.constraints.NotNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    private final MercadoPagoGatewayService mercadoPagoGatewayService;

    private final int orderExpirationMinutes;

    public OrderServiceImpl(
            ProductRepository productRepository,
            OrderRepository orderRepository,
            MercadoPagoGatewayService mercadoPagoGatewayService,
            @Value("${order-expiration-minutes}") int orderExpirationMinutes
    ) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.mercadoPagoGatewayService = mercadoPagoGatewayService;
        this.orderExpirationMinutes = orderExpirationMinutes;
    }

    @Override
    public Order create(@NotNull OrderCreateRequest orderCreateRequest) {

        val products = orderCreateRequest.getProducts().keySet().stream()
                .map(productRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (products.size() != orderCreateRequest.getProducts().size()) {
            val missingProductIds = orderCreateRequest.getProducts().keySet().stream()
                    .filter(productId -> products.stream().noneMatch(product -> product.getId().equals(productId)))
                    .toList();
            throw new ProductNotFoundException("Product not found: " + missingProductIds.stream().map(String::valueOf).toList());
        }

        if (products.stream().anyMatch(product -> orderCreateRequest.getProducts().get(product.getId()) <= 0)) {
            throw new InvalidOrderProductAmountException("Product amount must be greater than 0");
        }

        List<OrderProduct> orderProducts = products.stream()
                .map(product -> new OrderProduct(
                        null,
                        product,
                        orderCreateRequest.getProducts().get(product.getId())
                ))
                .toList();

        String link = generateUniqueLink();
        int totalPrice = calculateTotalPrice(orderProducts);

        val gatewayPayment = mercadoPagoGatewayService.createPayment(link, orderCreateRequest.getPlayerName(), totalPrice);

        val order = Order.builder()
                .expiration(LocalDateTime.now().plusMinutes(orderExpirationMinutes))
                .products(orderProducts)
                .orderStage(OrderStage.PENDING_PAYMENT)
                .playerName(orderCreateRequest.getPlayerName())
                .price(totalPrice)
                .gatewayPayment(gatewayPayment)
                .link(generateUniqueLink())
                .discount(0)
                .build();

        return orderRepository.save(order);
    }

    @Override
    public Order getOrderByLink(String link) {

        val order = orderRepository.findOrderByLink(link).orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (order.getExpiration().isBefore(LocalDateTime.now())) {
            order.changeState(OrderStage.EXPIRED);
            return orderRepository.save(order);
        }

        return order;
    }

    @Override
    public List<Order> getAllToDeliver() {
        val waiting = orderRepository.findAllByOrderStage(OrderStage.WAITING_DELIVERY);

        waiting.forEach(order -> {
            order.changeState(OrderStage.DELIVERED);
            orderRepository.save(order);
        });

        return waiting;
    }

    private int calculateTotalPrice(List<OrderProduct> products) {
        return products.stream()
                .mapToInt(orderProduct -> orderProduct.getProduct().getPrice() * orderProduct.getAmount())
                .sum();
    }

    private String generateUniqueLink() {
        String link;
        do {
            link = RandomUtils.getRandomString(10, 15);
        } while (orderRepository.findOrderByLink(link).isPresent());
        return link;
    }

    @Scheduled(fixedRate = 30000)
    public void checkExpiredOrders() {
        System.out.println("Checking expired orders");
        val expiredOrders = orderRepository.findAllByOrderStageAndExpirationBefore(OrderStage.PENDING_PAYMENT, LocalDateTime.now());

        System.out.println("expired: " + expiredOrders.size());

        expiredOrders.forEach(order -> {
            order.changeState(OrderStage.EXPIRED);
            orderRepository.save(order);
        });
    }

    @Scheduled(fixedRate = 30000)
    public void checkAllNonExpired() {
        System.out.println("Checking all non expired orders");
        val nonExpiredOrders = orderRepository.findAllByOrderStage(OrderStage.PENDING_PAYMENT);

        System.out.println("all: " + orderRepository.findAll().size());
        System.out.println("n expired: " + nonExpiredOrders);

        nonExpiredOrders.forEach(order -> {

            if (mercadoPagoGatewayService.paid(order.getGatewayPayment().getGatewayOrderId())) {
                System.out.println("PAID!");
            } else {
                System.out.println("NOT PAID!");
            }

        });
    }

}
