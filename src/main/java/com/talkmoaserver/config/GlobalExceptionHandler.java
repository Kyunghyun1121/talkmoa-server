package com.talkmoaserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    record ErrorResponse(String errorMessage) {}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exHandle(Exception ex) {
        log.warn("예외 발생, 핸들함 {} = {}", ex.getClass().getName(), ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ex.getMessage()));
    }
}
