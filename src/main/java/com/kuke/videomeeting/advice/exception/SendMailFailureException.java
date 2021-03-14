package com.kuke.videomeeting.advice.exception;

public class SendMailFailureException extends RuntimeException {
    public SendMailFailureException() {
        super();
    }

    public SendMailFailureException(String message) {
        super(message);
    }

    public SendMailFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
