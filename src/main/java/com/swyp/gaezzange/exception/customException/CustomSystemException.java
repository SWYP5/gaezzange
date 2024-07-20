package com.swyp.gaezzange.exception.customException;

import lombok.Getter;

public class CustomSystemException extends RuntimeException {

  @Getter
  String code;

  public CustomSystemException(String code, String message) {
    super(message);
    this.code = code;
  }

}
