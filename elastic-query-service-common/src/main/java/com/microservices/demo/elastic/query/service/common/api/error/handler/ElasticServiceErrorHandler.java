package com.microservices.demo.elastic.query.service.common.api.error.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ElasticServiceErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticServiceErrorHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handler(AccessDeniedException e){
        LOG.info("Access denied exception!", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized  to accsess this request");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handler(IllegalArgumentException e){
        LOG.info("Illegal denied exception!", e);
        return ResponseEntity.badRequest().body("Illegal denied exception! " + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handler(RuntimeException e){
        LOG.info("Service runtime exception!", e);
        return ResponseEntity.badRequest().body("Service runtime exception! " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handler(Exception e){
        LOG.info("Internal service exception!", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A server error occurred");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handler(MethodArgumentNotValidException e){
        LOG.info("Method argument validation exception!", e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error ->
                errors.put(((FieldError)error).getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}
