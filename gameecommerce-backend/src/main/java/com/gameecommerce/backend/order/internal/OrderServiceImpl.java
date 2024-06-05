package com.gameecommerce.backend.order.internal;

import com.gameecommerce.backend.order.*;
import com.gameecommerce.backend.order.exception.InvalidOrderProductAmountException;
import com.gameecommerce.backend.product.Product;
import com.gameecommerce.backend.product.ProductRepository;
import com.gameecommerce.backend.product.ProductService;
import com.gameecommerce.backend.product.exception.ProductNotFoundException;
import com.gameecommerce.backend.utils.RandomUtils;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    private final int orderExpirationMinutes;

    public OrderServiceImpl(ProductRepository productRepository, OrderRepository orderRepository, @Value("${order-expiration-minutes}") int orderExpirationMinutes) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderExpirationMinutes = orderExpirationMinutes;
    }

    @Override
    public Order create(@NotNull OrderCreateRequest orderCreateRequest) {

        val products = orderCreateRequest.getProductIds().keySet().stream()
                .map(productRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (products.size() != orderCreateRequest.getProductIds().size()) {
            val missingProductIds = orderCreateRequest.getProductIds().keySet().stream()
                    .filter(productId -> products.stream().noneMatch(product -> product.getId().equals(productId)))
                    .toList();
            throw new ProductNotFoundException("Product not found: " + missingProductIds.stream().map(String::valueOf).toList());
        }

        if (products.stream().anyMatch(product -> orderCreateRequest.getProductIds().get(product.getId()) <= 0)) {
            throw new InvalidOrderProductAmountException("Product amount must be greater than 0");
        }

        List<OrderProduct> orderProducts = products.stream()
                .map(product -> new OrderProduct(
                        null,
                        product,
                        orderCreateRequest.getProductIds().get(product.getId())
                ))
                .toList();

        if (orderCreateRequest.getCoupon() != null) {
            // TODO: implement coupon validation
        }

        val order = Order.builder()
                .expiration(LocalDateTime.now().plusMinutes(orderExpirationMinutes))
                .products(orderProducts)
                .orderStage(OrderStage.PENDING_PAYMENT)
                .playerName(orderCreateRequest.getPlayerName())
                .price(calculateTotalPrice(orderProducts))
                .link(RandomUtils.getRandomString(4, 10))
                .pixQrCode("pix")
                .discount(0)
                .build();

        orderRepository.save(order);

        return order;
    }

    @Override
    public Order getOrderById(@NotNull UUID id) {
        return null;
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

}
