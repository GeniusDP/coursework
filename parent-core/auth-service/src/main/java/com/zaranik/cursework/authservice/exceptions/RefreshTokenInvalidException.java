package com.zaranik.cursework.authservice.exceptions;

public class RefreshTokenInvalidException extends RuntimeException{
    public RefreshTokenInvalidException(String message) {
        super(message);
    }

    public RefreshTokenInvalidException() {
        super("Error: Refresh token has been expired, login pls");
    }
}
