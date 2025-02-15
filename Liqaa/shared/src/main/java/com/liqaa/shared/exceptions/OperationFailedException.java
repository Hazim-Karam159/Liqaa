package com.liqaa.shared.exceptions;

public class OperationFailedException extends ChatAppException {
    public OperationFailedException(String message) {
        super(message);
    }

    public OperationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}

