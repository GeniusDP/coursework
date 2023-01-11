package com.zaranik.coursework.checkerservice.exceptions;

import lombok.Getter;

@Getter
public class ContainerRuntimeException extends RuntimeException {

  private final int statusCode;

  public ContainerRuntimeException(int statusCode) {
    this.statusCode = statusCode;
  }

}
