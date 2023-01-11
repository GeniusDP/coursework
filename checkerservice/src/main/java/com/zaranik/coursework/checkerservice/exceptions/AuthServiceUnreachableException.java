package com.zaranik.coursework.checkerservice.exceptions;

public class AuthServiceUnreachableException extends RuntimeException {

  public AuthServiceUnreachableException() {
    super();
  }

  public AuthServiceUnreachableException(String message) {
    super(message);
  }
}
