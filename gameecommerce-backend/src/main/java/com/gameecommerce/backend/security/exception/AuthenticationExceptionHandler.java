package com.gameecommerce.backend.security.exception;

import com.gameecommerce.backend.exception.ExceptionResponse;
import com.gameecommerce.backend.exception.ExceptionResponseUtil;
import com.gameecommerce.backend.order.exception.InvalidOrderCouponException;
import com.gameecommerce.backend.order.exception.InvalidOrderProductAmountException;
import com.gameecommerce.backend.order.exception.InvalidOrderProductException;
import com.gameecommerce.backend.order.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException exception) {
        return ExceptionResponseUtil.buildResponseEntity(HttpStatus.FORBIDDEN, exception);
    }

}
