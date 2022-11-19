package com.example.demo.utils;

import com.example.demo.dto.AccessTokenInvalidException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

  @Value("${zaranik.app.jwtSecret}")
  private String jwtSecret;

  public String safeGetUserNameFromProbablyExpiredJwtToken(String token) {
    try {
      return Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
    } catch (ExpiredJwtException e) {
      return e.getClaims().getSubject();
    } catch (Exception e) {
      throw new AccessTokenInvalidException();
    }
  }

  public boolean tokenIsValid(String authToken) {
    try {
      Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(authToken);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean tokenIsExpiredButNotTampered(String authToken) {
    try {
      Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(authToken);
      return true;
    } catch (ExpiredJwtException e) {
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public String safeGetRoleNameFromProbablyExpiredToken(String token) {
    try {
      return Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody()
        .get("role", String.class);
    } catch (ExpiredJwtException e) {
      return e.getClaims().get("role", String.class);
    } catch (Exception e) {
      throw new AccessTokenInvalidException();
    }
  }
}
