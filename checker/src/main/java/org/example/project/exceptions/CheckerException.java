package org.example.project.exceptions;

public class CheckerException extends RuntimeException {

  public CheckerException() {
    super();
  }

  public CheckerException(String message) {
    super(message);
  }

  public CheckerException(Throwable cause) {
    super(cause);
  }
}
