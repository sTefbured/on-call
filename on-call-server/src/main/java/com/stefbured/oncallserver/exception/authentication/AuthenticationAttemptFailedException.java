package com.stefbured.oncallserver.exception.authentication;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationAttemptFailedException extends AuthenticationException {
    public AuthenticationAttemptFailedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AuthenticationAttemptFailedException(String msg) {
        super(msg);
    }
}
