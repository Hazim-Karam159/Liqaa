package com.liqaa.shared.exceptions;

import java.io.Serializable;

// Base exception
public class ChatAppException extends Exception implements Serializable
{
    public ChatAppException(String message)
    {
        super(message);
    }

    public ChatAppException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
