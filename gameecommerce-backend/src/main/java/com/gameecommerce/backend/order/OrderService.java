package com.gameecommerce.backend.order;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order create(@NotNull OrderCreateRequest orderCreateRequest);

    Order getOrderByLink(@NotNull String link);

    List<Order> getAllToDeliver();

}
