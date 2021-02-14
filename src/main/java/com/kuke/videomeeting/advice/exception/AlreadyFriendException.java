package com.kuke.videomeeting.advice.exception;

public class AlreadyFriendException extends RuntimeException {
    public AlreadyFriendException() {
    }

    public AlreadyFriendException(String message) {
        super(message);
    }

    public AlreadyFriendException(String message, Throwable cause) {
        super(message, cause);
    }
}
