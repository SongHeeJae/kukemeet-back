package com.kuke.videomeeting.advice.exception;

public class LockedAccountException extends RuntimeException {
    public LockedAccountException() {
        super();
    }

    public LockedAccountException(String message) {
        super(message);
    }

    public LockedAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
