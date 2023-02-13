package dev.agaber.vote.service.server;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handles exceptions thrown by other controllers and returns a standardized
 * HTTP response.
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
  @ResponseStatus(value= HttpStatus.BAD_REQUEST)  // 409
  @ExceptionHandler(IllegalArgumentException.class)
  public void illegalArgument() {
    // pass
  }
}
