package com.stefbured.oncallserver.exception;

public class RedirectException extends RuntimeException {
    public RedirectException(String message) {
        super(message);
    }

    public RedirectException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedirectException() {
        super("Exception during redirect");
    }
}
