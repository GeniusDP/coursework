package com.zaranik.cursework.authservice.exceptions;

public class LoginException extends RuntimeException{

    public LoginException(String message) {
        super(message);
    }

    public LoginException() {
        super("Wrong input data.");
    }
}
