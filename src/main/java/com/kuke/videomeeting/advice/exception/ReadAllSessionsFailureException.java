package com.kuke.videomeeting.advice.exception;

public class ReadAllSessionsFailureException extends RuntimeException {
    public ReadAllSessionsFailureException() {
        super();
    }

    public ReadAllSessionsFailureException(String message) {
        super(message);
    }

    public ReadAllSessionsFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
