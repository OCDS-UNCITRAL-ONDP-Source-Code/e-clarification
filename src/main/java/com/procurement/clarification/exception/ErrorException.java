package com.procurement.clarification.exception;

import lombok.Getter;

@Getter
public class ErrorException extends RuntimeException {

    private final String message;

    public ErrorException(final String message) {
        this.message = message;
    }
}