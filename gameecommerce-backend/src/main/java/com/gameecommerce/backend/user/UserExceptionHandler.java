package com.gameecommerce.backend.user;

import com.gameecommerce.backend.exception.ExceptionResponse;
import com.gameecommerce.backend.user.exception.InvalidUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {


    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidUserException(Us exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message(exception.getMessage())
                        .build());
    }


}
