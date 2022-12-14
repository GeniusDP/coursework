package com.example.solutionservice.exceptions;

public class SolutionNotFoundException extends RuntimeException {

  public SolutionNotFoundException() {
  }

  public SolutionNotFoundException(Throwable cause) {
    super(cause);
  }
}
