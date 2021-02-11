package com.kuke.videomeeting.advice.exception;

public class UserUidAlreadyExistsException extends RuntimeException {
    public UserUidAlreadyExistsException() {
    }

    public UserUidAlreadyExistsException(String message) {
        super(message);
    }

    public UserUidAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
