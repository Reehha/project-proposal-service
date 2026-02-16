package com.example.projectproposal.dto;

public class AuthResponse {
  public String token;
  public String uid;
  public String email;
  public String role;

  public AuthResponse(String token, String uid, String email, String role) {
    this.token = token;
    this.uid = uid;
    this.email = email;
    this.role = role;
  }
}
