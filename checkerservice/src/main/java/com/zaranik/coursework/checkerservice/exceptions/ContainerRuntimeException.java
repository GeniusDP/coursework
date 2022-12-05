package com.zaranik.coursework.checkerservice.exceptions;

public class ContainerRuntimeException extends RuntimeException {

  public ContainerRuntimeException() {
    super();
  }

  public ContainerRuntimeException(String message) {
    super(message);
  }

  public ContainerRuntimeException(Throwable cause) {
    super(cause);
  }
}
