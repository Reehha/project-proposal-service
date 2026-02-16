package com.example.projectproposal.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.example.projectproposal.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
        .collect(Collectors.toMap(FieldError::getField, fe -> fe.getDefaultMessage() == null ? "Invalid" : fe.getDefaultMessage(), (a, b) -> a));

    var body = ErrorResponse.basic(400, "Bad Request", "Validation failed", req.getRequestURI());
    body.fieldErrors = fieldErrors;
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(SecurityException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorized(SecurityException ex, HttpServletRequest req) {
    var body = ErrorResponse.basic(401, "Unauthorized", ex.getMessage(), req.getRequestURI());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(IllegalArgumentException ex, HttpServletRequest req) {
    String msg = ex.getMessage() == null ? "Bad request" : ex.getMessage();
    var body = ErrorResponse.basic(400, "Bad Request", msg, req.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
    var body = ErrorResponse.basic(500, "Internal Server Error", "Unexpected error", req.getRequestURI());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }
}
