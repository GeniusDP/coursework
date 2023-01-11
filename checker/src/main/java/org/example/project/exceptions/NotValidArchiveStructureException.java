package org.example.project.exceptions;

public class NotValidArchiveStructureException extends RuntimeException {

  public NotValidArchiveStructureException() {
    super();
  }

  public NotValidArchiveStructureException(String message) {
    super(message);
  }
}
