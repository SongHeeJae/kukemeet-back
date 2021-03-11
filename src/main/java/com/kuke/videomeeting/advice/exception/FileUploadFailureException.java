package com.kuke.videomeeting.advice.exception;

public class FileUploadFailureException extends RuntimeException {
    public FileUploadFailureException() {
    }

    public FileUploadFailureException(String message) {
        super(message);
    }

    public FileUploadFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
