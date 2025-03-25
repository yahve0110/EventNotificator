package com.yahve.eventnotificator.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class JwtTokenManager {

  private final SecretKey key;

  public JwtTokenManager(
    @Value("${jwt.secret-key}") String keyString,
    @Value("${jwt.lifetime}") long expirationTime
  ) {
    this.key = Keys.hmacShaKeyFor(keyString.getBytes());
  }

  public Long getUserId(String token) {
    return Long.valueOf(Jwts.parser()
      .verifyWith(key)
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .get("userId")
      .toString());
  }

  public String getLogin(String token) {
    return Jwts.parser()
      .verifyWith(key)
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .getSubject();
  }

  public String getRole(String token) {
    return Jwts.parser()
      .verifyWith(key)
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .get("role", String.class);
  }
}
