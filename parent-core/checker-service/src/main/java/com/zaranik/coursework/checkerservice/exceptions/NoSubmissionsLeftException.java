package com.zaranik.coursework.checkerservice.exceptions;

public class NoSubmissionsLeftException extends RuntimeException {

  public NoSubmissionsLeftException() {
    super();
  }

  public NoSubmissionsLeftException(String message) {
    super(message);
  }

  public NoSubmissionsLeftException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoSubmissionsLeftException(Throwable cause) {
    super(cause);
  }
}
