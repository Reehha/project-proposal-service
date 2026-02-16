package com.example.projectproposal.service;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.projectproposal.dto.AuthResponse;
import com.example.projectproposal.dto.LoginRequest;
import com.example.projectproposal.dto.RegisterRequest;
import com.example.projectproposal.model.AppUser;
import com.example.projectproposal.repository.UserRepository;
import com.example.projectproposal.security.JwtService;
import com.example.projectproposal.util.IdUtil;

@Service
public class AuthService {

  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final JwtService jwt;

  public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
    this.users = users;
    this.encoder = encoder;
    this.jwt = jwt;
  }

  public AuthResponse register(RegisterRequest req) {
    String emailLower = req.email.trim().toLowerCase(Locale.ROOT);
    users.findByEmail(emailLower).ifPresent(u -> {
      throw new IllegalArgumentException("Email already registered");
    });

    AppUser u = new AppUser();
    u.setUid(IdUtil.newUserId());
    u.setFirstName(req.firstName.trim());
    u.setLastName(req.lastName.trim());
    u.setDob(req.dob);
    u.setEmail(emailLower);
    u.setGender(req.gender.trim());
    u.setPhoneNumber(req.phoneNumber.trim());
    u.setNationality(req.nationality.trim());
    u.setAddress(req.address.trim());
    u.setRole(req.role.trim().toUpperCase(Locale.ROOT));
    u.setPasswordHash(encoder.encode(req.password));

    users.save(u);

    // Optional: auto-login on register (returns token)
    String token = jwt.generate(u.getUid(), u.getEmail(), u.getRole());
    return new AuthResponse(token, u.getUid(), u.getEmail(), u.getRole());
  }

  public AuthResponse login(LoginRequest req) {
    String emailLower = req.email.trim().toLowerCase(Locale.ROOT);
    AppUser u = users.findByEmail(emailLower)
        .orElseThrow(() -> new SecurityException("Invalid credentials"));

    if (!encoder.matches(req.password, u.getPasswordHash())) {
      throw new SecurityException("Invalid credentials");
    }

    String token = jwt.generate(u.getUid(), u.getEmail(), u.getRole());
    return new AuthResponse(token, u.getUid(), u.getEmail(), u.getRole());
  }
}
