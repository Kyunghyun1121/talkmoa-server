package com.talkmoaserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    record ErrorResponse(String errorMessage) {}

    @ExceptionHandler(Exception.class)
    public String exHandle(Exception ex) {
        log.warn("예외 발생, 핸들함 {} = {}", ex.getClass().getName(), ex.getMessage());
        return "error";
    }
}
