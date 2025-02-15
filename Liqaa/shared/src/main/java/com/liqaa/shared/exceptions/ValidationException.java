package com.liqaa.shared.exceptions;

public class ValidationException extends ChatAppException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

