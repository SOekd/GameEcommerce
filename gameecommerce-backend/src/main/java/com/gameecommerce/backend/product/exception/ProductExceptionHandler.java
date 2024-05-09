package com.gameecommerce.backend.product.exception;

import com.gameecommerce.backend.exception.ExceptionResponse;
import com.gameecommerce.backend.exception.ExceptionResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidProductException(InvalidProductException exception) {
        return ExceptionResponseUtil.buildResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleProductNotFoundException(ProductNotFoundException exception) {
        return ExceptionResponseUtil.buildResponseEntity(HttpStatus.NOT_FOUND, exception);
    }

}
