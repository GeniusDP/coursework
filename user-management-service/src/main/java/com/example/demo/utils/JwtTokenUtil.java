package com.example.demo.utils;

import com.example.demo.entities.RoleValue;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

  private final String jwtSecret;

  private final int jwtAccessTokenExpirationMs;

  private final int jwtRefreshTokenExpirationMs;

  public JwtTokenUtil(@Value("${zaranik.app.jwtSecret}") String jwtSecret,
      @Value("${zaranik.app.jwtAccessTokenExpirationSeconds}") int jwtAccessTokenExpirationMs,
      @Value("${zaranik.app.jwtRefreshTokenExpirationSeconds}") int jwtRefreshTokenExpirationMs) {
    this.jwtSecret = jwtSecret;
    this.jwtAccessTokenExpirationMs = jwtAccessTokenExpirationMs;
    this.jwtRefreshTokenExpirationMs = jwtRefreshTokenExpirationMs;
  }

  public String safeGetUserNameFromProbablyExpiredJwtToken(String token) {
    try {
      return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    } catch (ExpiredJwtException e) {
      return e.getClaims().getSubject();
    } catch (Exception e) {
      return null;
    }
  }

  public boolean tokenIsValid(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean tokenIsExpiredButNotTampered(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (ExpiredJwtException e) {
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public String safeGetRoleNameFromProbablyExpiredToken(String token) {
    try {
      return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody()
          .get("role", String.class);
    } catch (ExpiredJwtException e) {
      return e.getClaims().get("role", String.class);
    } catch (Exception e) {
      return null;
    }
  }
}
