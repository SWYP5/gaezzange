package com.swyp.gaezzange.exception;

import com.swyp.gaezzange.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse handleException(Exception ex, WebRequest request) {
    log.error("Exception occurs.", ex);

    return ApiResponse.fail("ERROR", ex.getMessage(), null);
  }
}