package com.example.projectproposal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.projectproposal.dto.AuthResponse;
import com.example.projectproposal.dto.LoginRequest;
import com.example.projectproposal.dto.RegisterRequest;
import com.example.projectproposal.service.AuthService;

import jakarta.validation.Valid;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

  private final AuthService auth;
  private final DynamoDbClient dynamoDbClient;

  public AuthController(AuthService auth, DynamoDbClient dynamoDbClient) {
    this.auth = auth;
    this.dynamoDbClient = dynamoDbClient;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
    var res = auth.register(req);
    return ResponseEntity.status(HttpStatus.CREATED).body(res);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
    return ResponseEntity.ok(auth.login(req));
  }


}
