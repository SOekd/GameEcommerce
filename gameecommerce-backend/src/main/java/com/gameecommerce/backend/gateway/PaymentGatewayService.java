package com.gameecommerce.backend.gateway;

public interface PaymentGatewayService {

    GatewayPayment createPayment(String link, String player, long price);

    boolean paid(String id);

}
