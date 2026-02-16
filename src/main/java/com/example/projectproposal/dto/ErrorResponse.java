package com.example.projectproposal.dto;

import java.time.Instant;
import java.util.Map;

public class ErrorResponse {
  public Instant timestamp = Instant.now();
  public int status;
  public String error;
  public String message;
  public String path;
  public Map<String, String> fieldErrors;

  public static ErrorResponse basic(int status, String error, String message, String path) {
    var r = new ErrorResponse();
    r.status = status;
    r.error = error;
    r.message = message;
    r.path = path;
    return r;
  }
}
