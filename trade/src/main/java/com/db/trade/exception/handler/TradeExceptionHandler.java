package com.db.trade.exception.handler;

import com.db.trade.exception.InvalidTradeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.InvalidClassException;

@RestControllerAdvice
@Slf4j
public class TradeExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InvalidTradeException.class)
    public ResponseEntity<String> handleException(InvalidTradeException exception){
      log.error(exception.getMessage());
      return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
