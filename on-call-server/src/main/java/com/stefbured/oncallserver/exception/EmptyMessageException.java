package com.stefbured.oncallserver.exception;

public class EmptyMessageException extends RuntimeException {
    public EmptyMessageException(String message) {
        super(message);
    }

    public EmptyMessageException() {
        super("Message is empty.");
    }

    public EmptyMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
