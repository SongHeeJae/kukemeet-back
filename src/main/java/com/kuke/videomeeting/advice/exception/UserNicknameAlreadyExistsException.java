package com.kuke.videomeeting.advice.exception;

public class UserNicknameAlreadyExistsException extends RuntimeException {
    public UserNicknameAlreadyExistsException() {
    }

    public UserNicknameAlreadyExistsException(String message) {
        super(message);
    }

    public UserNicknameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
