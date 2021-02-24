package com.kuke.videomeeting.advice.exception;

public class ReadAllRoomsFailureException extends RuntimeException {
    public ReadAllRoomsFailureException() {
    }

    public ReadAllRoomsFailureException(String message) {
        super(message);
    }

    public ReadAllRoomsFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
