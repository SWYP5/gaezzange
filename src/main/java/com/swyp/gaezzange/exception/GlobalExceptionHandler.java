package com.swyp.gaezzange.exception;

import com.swyp.gaezzange.exception.customException.BizException;
import com.swyp.gaezzange.exception.customException.CustomSystemException;
import com.swyp.gaezzange.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  @ExceptionHandler(value = { BizException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse handleCustomException(BizException ex) {
    return ApiResponse.fail(ex.getCode(), ex.getMessage(), null);
  }

  @ExceptionHandler(value = { CustomSystemException.class })
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse handleCustomException(CustomSystemException ex) {
    return ApiResponse.fail(ex.getCode(), ex.getMessage(), null);
  }

  @ExceptionHandler(value = { Exception.class })
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse handleException(Exception ex) {
    log.error("", ex);
    return ApiResponse.fail("ERROR", ex.getMessage(), null);
  }
}