package com.gameecommerce.backend.order.exception;

public class InvalidOrderProductException extends RuntimeException {

    public InvalidOrderProductException(String message) {
        super(message);
    }

}
