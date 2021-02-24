package com.kuke.videomeeting.advice.exception;

public class DestroyRoomFailureException extends RuntimeException {
    public DestroyRoomFailureException() {
    }

    public DestroyRoomFailureException(String message) {
        super(message);
    }

    public DestroyRoomFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
