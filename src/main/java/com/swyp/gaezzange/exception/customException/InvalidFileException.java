package com.swyp.gaezzange.exception.customException;

public class InvalidFileException extends CustomSystemException {
  public InvalidFileException(String message) {
    super("INVALID_FILE", message);
  }
}
