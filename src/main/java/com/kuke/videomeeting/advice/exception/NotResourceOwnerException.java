package com.kuke.videomeeting.advice.exception;

public class NotResourceOwnerException extends RuntimeException {
    public NotResourceOwnerException() {
    }

    public NotResourceOwnerException(String message) {
        super(message);
    }

    public NotResourceOwnerException(String message, Throwable cause) {
        super(message, cause);
    }
}
