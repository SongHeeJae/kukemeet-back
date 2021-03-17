package com.kuke.videomeeting.advice.exception;

public class NotRegisteredProviderUserInfoException extends RuntimeException {
    public NotRegisteredProviderUserInfoException() {
        super();
    }

    public NotRegisteredProviderUserInfoException(String message) {
        super(message);
    }

    public NotRegisteredProviderUserInfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
