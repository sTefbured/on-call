package com.stefbured.oncallserver.exception.user;

public class InvalidUserParametersException extends UserException {
    public InvalidUserParametersException(String message) {
        super(message);
    }

    public InvalidUserParametersException(String message, Throwable cause) {
        super(message, cause);
    }
}
