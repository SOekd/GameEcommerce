package com.gameecommerce.backend.order.internal;

import com.gameecommerce.backend.gateway.impl.mercadopago.MercadoPagoGatewayService;
import com.gameecommerce.backend.order.*;
import com.gameecommerce.backend.order.exception.InvalidDeliverException;
import com.gameecommerce.backend.order.exception.InvalidOrderProductAmountException;
import com.gameecommerce.backend.order.exception.OrderNotFoundException;
import com.gameecommerce.backend.product.ProductRepository;
import com.gameecommerce.backend.product.exception.ProductNotFoundException;
import com.gameecommerce.backend.utils.RandomUtils;
import jakarta.validation.constraints.NotNull;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        double totalPrice = calculateTotalPrice(orderProducts);

        val gatewayPayment = mercadoPagoGatewayService.createPayment(link, orderCreateRequest.getPlayerName(), totalPrice);

        val order = Order.builder()
                .expiration(LocalDateTime.now().plusMinutes(orderExpirationMinutes))
                .products(orderProducts)
                .state(OrderState.PENDING_PAYMENT)
                .playerName(orderCreateRequest.getPlayerName())
                .price(totalPrice)
                .paymentGateway(gatewayPayment)
                .link(generateUniqueLink())
                .build();

        return orderRepository.save(order);
    }

    @Override
    public Order getOrderByLink(String link) {

        val order = orderRepository.findOrderByLink(link).orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (order.getExpiration().isBefore(LocalDateTime.now())) {
            order.changeState(OrderState.EXPIRED);
            return orderRepository.save(order);
        }

        return order;
    }

    @Override
    public OrderSearchResponse search(OrderSearchRequest orderSearchRequest) {

        val page = PageRequest.of(
                orderSearchRequest.getPage() - 1,
                orderSearchRequest.getItemsPerPage(),
                Sort.by(orderSearchRequest.getSortDirection(), orderSearchRequest.getSortColumn())
        );


        val states = orderSearchRequest.getStates();

        if (states.isEmpty()) {
            states.addAll(List.of(OrderState.values()));
        }

        if (orderSearchRequest.getSearch() == null || orderSearchRequest.getSearch().isBlank()) {
            return new OrderSearchResponse(
                    orderRepository.findAllByStateIn(states, page),
                    orderRepository.countAllByStateIn(states)
            );
        }

        val searchName = "%" + orderSearchRequest.getSearch() + "%";
        return new OrderSearchResponse(
                orderRepository.findAllByPlayerNameLikeAndStateIn(searchName, states, page),
                orderRepository.countAllByPlayerNameLikeAndStateIn(searchName, states)
        );
    }

    @Override
    public Order deliver(UUID id) {
        val order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (order.getState() != OrderState.WAITING_DELIVERY) {
            throw new InvalidDeliverException("Order must be in WAITING_DELIVERY state");
        }

        order.changeState(OrderState.DELIVERED);
        return orderRepository.save(order);
    }

    private double calculateTotalPrice(List<OrderProduct> products) {
        return products.stream()
                .mapToDouble(orderProduct -> orderProduct.getProduct().getPrice() * orderProduct.getAmount())
                .sum();
    }

    private String generateUniqueLink() {
        String link;
        do {
            link = RandomUtils.getRandomString(10, 15);
        } while (orderRepository.findOrderByLink(link).isPresent());
        return link;
    }

    @Scheduled(fixedRate = 10000)
    public void checkOrders() {
        val expiredOrders = orderRepository.findAllByStateAndExpirationBefore(OrderState.PENDING_PAYMENT, LocalDateTime.now());

        expiredOrders.forEach(order -> {
            order.changeState(OrderState.EXPIRED);
            orderRepository.save(order);
        });

        val nonExpiredOrders = orderRepository.findAllByState(OrderState.PENDING_PAYMENT);

        nonExpiredOrders.forEach(order -> {

            val stage = mercadoPagoGatewayService.getStage(order.getPaymentGateway().getGatewayOrderId());

            if (stage == null || stage == OrderState.PENDING_PAYMENT) {
                return;
            }

            order.changeState(stage);
            orderRepository.save(order);
        });

    }


}
