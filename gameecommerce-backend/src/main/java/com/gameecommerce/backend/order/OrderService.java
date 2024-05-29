package com.gameecommerce.backend.order;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface OrderService {

    Order create(@NotNull OrderCreateRequest orderCreateRequest);

    Order getOrderById(@NotNull UUID id);

}
