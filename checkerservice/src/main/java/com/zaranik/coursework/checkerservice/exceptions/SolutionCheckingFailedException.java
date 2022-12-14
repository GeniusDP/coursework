package com.zaranik.coursework.checkerservice.exceptions;

public class SolutionCheckingFailedException extends RuntimeException {

  public SolutionCheckingFailedException() {
    super();
  }

  public SolutionCheckingFailedException(String message) {
    super(message);
  }

  public SolutionCheckingFailedException(String message, Throwable cause) {
    super(message, cause);
  }

  public SolutionCheckingFailedException(Throwable cause) {
    super(cause);
  }
}
