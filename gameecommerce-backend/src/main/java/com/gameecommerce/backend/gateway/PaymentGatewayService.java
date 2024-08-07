package com.gameecommerce.backend.gateway;

import com.gameecommerce.backend.order.OrderState;
import org.jetbrains.annotations.Nullable;

public interface PaymentGatewayService {

    PaymentGateway createPayment(String link, String player, double price);

    @Nullable
    OrderState getStage(String id);

}
