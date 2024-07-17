package com.swyp.gaezzange.exception.customException;

import lombok.Getter;

public class BizException extends RuntimeException {

  @Getter
  String code;

  public BizException(String code, String message) {
    super(message);
    this.code = code;
  }

}
