package com.example.demo.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class JwtTokenUtilTest {

  @Test
  void safeGetUserNameFromProbablyExpiredJwtToken() {
    JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(new ObjectMapper());
    String token = "eyJzdWIiOiJ1c3IiLC.eyJzdWIiOiJ1c3IiLCJpYXQiOjE2NjgwODE1NzUsImV4cCI6MTY2ODA4MTYzNSwicm9sZSI6IkFETUlOIn0.eyJzdWIiOiJ1c3IiLC";
    String name = jwtTokenUtil.getUserNameFromToken(token);
    assertEquals(name, "usr");
  }

  @Test
  void safeGetRoleNameFromProbablyExpiredToken() {
    JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(new ObjectMapper());
    String token = "eyJzdWIiOiJ1c3IiLC.eyJzdWIiOiJ1c3IiLCJpYXQiOjE2NjgwODE1NzUsImV4cCI6MTY2ODA4MTYzNSwicm9sZSI6IkFETUlOIn0.eyJzdWIiOiJ1c3IiLC";
    String role = jwtTokenUtil.getRoleNameFromToken(token);
    assertEquals(role, "ADMIN");
  }

}