package com.kuke.videomeeting.advice.exception;

public class NotRegisteredProviderException extends RuntimeException {
    public NotRegisteredProviderException() {
        super();
    }

    public NotRegisteredProviderException(String message) {
        super(message);
    }

    public NotRegisteredProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
