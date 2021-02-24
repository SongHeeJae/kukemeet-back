package com.kuke.videomeeting.advice.exception;

public class CreateRoomFailureException extends RuntimeException {

    public CreateRoomFailureException() {
    }

    public CreateRoomFailureException(String message) {
        super(message);
    }

    public CreateRoomFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
