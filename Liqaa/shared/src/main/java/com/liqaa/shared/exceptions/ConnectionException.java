package com.liqaa.shared.exceptions;

public class ConnectionException  extends ChatAppException {
    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }


}

