package com.stefbured.oncallserver.exception;

public class ExternalSystemRequestException extends RuntimeException {
    public ExternalSystemRequestException(String message) {
        super(message);
    }

    public ExternalSystemRequestException() {
        super("Error while processing request to external system");
    }

    public ExternalSystemRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
