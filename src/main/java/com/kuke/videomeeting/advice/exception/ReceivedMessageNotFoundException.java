package com.kuke.videomeeting.advice.exception;

public class ReceivedMessageNotFoundException extends RuntimeException{
    public ReceivedMessageNotFoundException() {
    }

    public ReceivedMessageNotFoundException(String message) {
        super(message);
    }

    public ReceivedMessageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
