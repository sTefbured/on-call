package com.stefbured.oncallserver.exception.primarykey;

public class PrimaryKeyGenerationTimeoutException extends PrimaryKeyGenerationException {
    public PrimaryKeyGenerationTimeoutException() {
        super("Timeout exception during primary key generation");
    }

    public PrimaryKeyGenerationTimeoutException(String message) {
        super(message);
    }

    public PrimaryKeyGenerationTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
