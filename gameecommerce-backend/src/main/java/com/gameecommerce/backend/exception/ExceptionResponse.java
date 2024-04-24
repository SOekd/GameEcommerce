package com.gameecommerce.backend.exception;

import lombok.Builder;

@Builder
public class ExceptionResponse {

    public final String message;
    public final int status;

}
