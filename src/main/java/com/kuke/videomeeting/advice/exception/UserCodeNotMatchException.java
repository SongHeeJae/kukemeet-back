package com.kuke.videomeeting.advice.exception;

public class UserCodeNotMatchException extends RuntimeException {
    public UserCodeNotMatchException() {
        super();
    }

    public UserCodeNotMatchException(String message) {
        super(message);
    }

    public UserCodeNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
