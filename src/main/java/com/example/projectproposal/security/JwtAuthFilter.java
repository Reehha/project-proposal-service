package com.example.projectproposal.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwt;

  public JwtAuthFilter(JwtService jwt) {
    this.jwt = jwt;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (!StringUtils.hasText(auth) || !auth.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = auth.substring("Bearer ".length()).trim();
    try {
      var claims = jwt.parse(token);
      String uid = claims.getSubject();
      String role = String.valueOf(claims.get("role"));

      var authToken = new UsernamePasswordAuthenticationToken(
          uid,
          null,
          java.util.List.of(new SimpleGrantedAuthority("ROLE_" + role))
      );
      SecurityContextHolder.getContext().setAuthentication(authToken);
      filterChain.doFilter(request, response);
    } catch (JwtException ex) {
      // invalid token -> clear context and continue (will be rejected by security rules)
      SecurityContextHolder.clearContext();
      filterChain.doFilter(request, response);
    }
  }
}
