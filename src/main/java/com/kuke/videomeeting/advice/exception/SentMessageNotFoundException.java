package com.kuke.videomeeting.advice.exception;

public class SentMessageNotFoundException extends RuntimeException {
    public SentMessageNotFoundException() {
    }

    public SentMessageNotFoundException(String message) {
        super(message);
    }

    public SentMessageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
