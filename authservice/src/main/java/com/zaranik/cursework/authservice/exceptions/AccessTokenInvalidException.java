package com.zaranik.cursework.authservice.exceptions;

public class AccessTokenInvalidException extends RuntimeException {
    public AccessTokenInvalidException() {
        super("Token is invalid (maybe expired or defected). Refresh it.");
    }

    public AccessTokenInvalidException(String message) {
        super(message);
    }

    public AccessTokenInvalidException(Exception e) {
        super(e);
    }
}
