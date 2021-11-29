package com.example.temperatureapis.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(HttpServerErrorException.class)
    protected ResponseEntity<String> handleHttpErrors(HttpServerErrorException exception) {
        log.error("http exception: {}", exception.toString());
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
    }


    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleInternalException(Exception exception) {
        log.error("internal exception", exception);
        return ResponseEntity.internalServerError().build();
    }
}
