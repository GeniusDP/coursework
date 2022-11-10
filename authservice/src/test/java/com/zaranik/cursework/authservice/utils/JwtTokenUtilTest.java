package com.zaranik.cursework.authservice.utils;

import com.zaranik.cursework.authservice.entities.RoleValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;


class JwtTokenUtilTest {

    private static final int WILL_NOT_EXPIRE = 1_000_000;
    private static final int ALREADY_EXPIRED = -1_000_000;

    String jwtSecret = "BogdanZaranik".repeat(20);
    String john123Username = "john123";

    @ParameterizedTest
    @NullAndEmptySource
    void cornerCases(String token){
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(jwtSecret, WILL_NOT_EXPIRE, WILL_NOT_EXPIRE);
        assertThat(jwtTokenUtil.tokenIsValid(token)).isFalse();
    }

    @Test
    void testExpirationOfToken() {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(jwtSecret, ALREADY_EXPIRED, WILL_NOT_EXPIRE);
        String token = jwtTokenUtil.generateAccessJwtToken(john123Username, RoleValue.STUDENT);
        assertThat(jwtTokenUtil.tokenIsValid(token)).isFalse();
    }

    @Test
    void testTamperedToken() {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(jwtSecret, WILL_NOT_EXPIRE, WILL_NOT_EXPIRE);
        String token = jwtTokenUtil.generateAccessJwtToken(john123Username, RoleValue.STUDENT);
        token = tamperToken(token);
        assertThat(jwtTokenUtil.tokenIsValid(token)).isFalse();
    }

    @Test
    void testsSafeExtractionOfSubjectFromExpiredToken() {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(jwtSecret, ALREADY_EXPIRED, WILL_NOT_EXPIRE);
        String token = jwtTokenUtil.generateAccessJwtToken(john123Username, RoleValue.STUDENT);

        Assertions.assertDoesNotThrow(() -> jwtTokenUtil.safeGetUserNameFromProbablyExpiredJwtToken(token));

        String userName = jwtTokenUtil.safeGetUserNameFromProbablyExpiredJwtToken(token);
        assertThat(userName).isEqualTo(john123Username);
    }

    @Test
    void testsSafeExtractionOfSubjectFromTamperedToken() {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(jwtSecret, ALREADY_EXPIRED, WILL_NOT_EXPIRE);
        String token = jwtTokenUtil.generateAccessJwtToken(john123Username, RoleValue.STUDENT);

        Assertions.assertDoesNotThrow(() -> jwtTokenUtil.safeGetUserNameFromProbablyExpiredJwtToken(token));

        String userName = jwtTokenUtil.safeGetUserNameFromProbablyExpiredJwtToken(token);
        assertThat(userName).isEqualTo(john123Username);
    }

    private String tamperToken(String token){
        String[] tokenParts = token.split("\\.");
        tokenParts[1] = "gbjsrhfvdshavFGgSGDfHhGuHsGFHGFHJdFuJhHgFJvfHFsJnFDvhJjFgHJsFdJqFHiJuHhrjqvtiughae";
        return String.join(".", tokenParts);
    }

}