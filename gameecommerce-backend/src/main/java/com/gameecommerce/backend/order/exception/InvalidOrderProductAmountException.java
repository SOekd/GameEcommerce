package com.gameecommerce.backend.order.exception;

public class InvalidOrderProductAmountException extends RuntimeException {

    public InvalidOrderProductAmountException(String message) {
        super(message);
    }

}
