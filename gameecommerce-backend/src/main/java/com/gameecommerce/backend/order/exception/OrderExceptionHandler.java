package com.gameecommerce.backend.order.exception;

import com.gameecommerce.backend.exception.ExceptionResponse;
import com.gameecommerce.backend.exception.ExceptionResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(InvalidOrderProductException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidOrderRequestException(InvalidOrderProductException exception) {
        return ExceptionResponseUtil.buildResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(InvalidOrderCouponException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidOrderCouponException(InvalidOrderCouponException exception) {
        return ExceptionResponseUtil.buildResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleOrderNotFoundException(OrderNotFoundException exception) {
        return ExceptionResponseUtil.buildResponseEntity(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(InvalidOrderProductAmountException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidOrderProductAmountException(InvalidOrderProductAmountException exception) {
        return ExceptionResponseUtil.buildResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(InvalidDeliverException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidDeliverException(InvalidDeliverException exception) {
        return ExceptionResponseUtil.buildResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }


}
