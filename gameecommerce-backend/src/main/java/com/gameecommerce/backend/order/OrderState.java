package com.gameecommerce.backend.order;

public enum OrderState {

    PENDING_PAYMENT,
    EXPIRED,
    REFUNDING,
    REFUNDED,
    WAITING_DELIVERY,
    DELIVERED,
    CANCELLED

}
