package com.kuke.videomeeting.advice.exception;

public class MediaServerCommunicationFailureException extends RuntimeException {
    public MediaServerCommunicationFailureException() {
        super();
    }

    public MediaServerCommunicationFailureException(String message) {
        super(message);
    }

    public MediaServerCommunicationFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
