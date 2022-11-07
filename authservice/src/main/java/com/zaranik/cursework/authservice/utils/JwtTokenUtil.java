package com.zaranik.cursework.authservice.utils;

import com.zaranik.cursework.authservice.exceptions.AccessTokenInvalidException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {

    private final String jwtSecret;

    private final int jwtAccessTokenExpirationMs;

    private final int jwtRefreshTokenExpirationMs;

    public JwtTokenUtil(@Value("${zaranik.app.jwtSecret}") String jwtSecret,
                        @Value("${zaranik.app.jwtAccessTokenExpirationSeconds}")int jwtAccessTokenExpirationMs,
                        @Value("${zaranik.app.jwtRefreshTokenExpirationSeconds}")int jwtRefreshTokenExpirationMs) {
        this.jwtSecret = jwtSecret;
        this.jwtAccessTokenExpirationMs = jwtAccessTokenExpirationMs;
        this.jwtRefreshTokenExpirationMs = jwtRefreshTokenExpirationMs;
    }

    public String generateAccessJwtToken(String username) {
        return generateJwtToken(username, jwtAccessTokenExpirationMs);
    }

    public String generateRefreshJwtToken(String username) {
        return generateJwtToken(username, jwtRefreshTokenExpirationMs);
    }

    private String generateJwtToken(String username, int expirationMillis) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationMillis * 1000L))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String safeGetUserNameFromProbablyExpiredJwtToken(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (Exception e) {
            throw new AccessTokenInvalidException(e);
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

}
