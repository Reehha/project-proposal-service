package com.example.projectproposal.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

  private final byte[] keyBytes;
  private final String issuer;
  private final long expMillis;

  public JwtService(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.issuer}") String issuer,
      @Value("${app.jwt.expMinutes}") long expMinutes
  ) {
    this.keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    this.issuer = issuer;
    this.expMillis = expMinutes * 60_000L;
  }

  public String generate(String uid, String email, String role) {
    var now = new Date();
    var exp = new Date(now.getTime() + expMillis);

    return Jwts.builder()
        .setIssuer(issuer)
        .setSubject(uid)
        .setIssuedAt(now)
        .setExpiration(exp)
        .addClaims(Map.of(
            "email", email,
            "role", role
        ))
        .signWith(Keys.hmacShaKeyFor(keyBytes))
        .compact();
  }

  public Claims parse(String token) {
    return Jwts.parserBuilder()
        .requireIssuer(issuer)
        .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
