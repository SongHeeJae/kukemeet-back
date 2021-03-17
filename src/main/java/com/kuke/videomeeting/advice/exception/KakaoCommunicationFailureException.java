package com.kuke.videomeeting.advice.exception;

public class KakaoCommunicationFailureException extends RuntimeException {
    public KakaoCommunicationFailureException() {
        super();
    }

    public KakaoCommunicationFailureException(String message) {
        super(message);
    }

    public KakaoCommunicationFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
