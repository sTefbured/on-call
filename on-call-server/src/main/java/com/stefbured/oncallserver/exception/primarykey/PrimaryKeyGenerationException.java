package com.stefbured.oncallserver.exception.primarykey;

public class PrimaryKeyGenerationException extends RuntimeException {
    public PrimaryKeyGenerationException() {
        super("Exception during primary key generation");
    }

    public PrimaryKeyGenerationException(String message) {
        super(message);
    }

    public PrimaryKeyGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
