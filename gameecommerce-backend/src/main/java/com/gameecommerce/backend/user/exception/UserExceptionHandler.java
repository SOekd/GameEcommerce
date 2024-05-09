package com.gameecommerce.backend.user.exception;

import com.gameecommerce.backend.exception.ExceptionResponse;
import com.gameecommerce.backend.exception.ExceptionResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidUserException(InvalidUserException exception) {
        return ExceptionResponseUtil.buildResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException exception) {
        return ExceptionResponseUtil.buildResponseEntity(HttpStatus.NOT_FOUND, exception);
    }

}
