package com.gameecommerce.backend.configuration.exception;

import com.gameecommerce.backend.exception.ExceptionResponse;
import com.gameecommerce.backend.exception.ExceptionResponseUtil;
import com.gameecommerce.backend.user.exception.InvalidUserException;
import com.gameecommerce.backend.user.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidUserException(InvalidUserException exception) {
        return ExceptionResponseUtil.buildResponseEntity(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        return ExceptionResponseUtil.buildResponseEntity(HttpStatus.CONFLICT, exception);
    }


}
