package com.zaranik.cursework.authservice.services;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaranik.cursework.authservice.dto.LoginUserDto;
import com.zaranik.cursework.authservice.dto.TokenDto;
import com.zaranik.cursework.authservice.entities.Role;
import com.zaranik.cursework.authservice.entities.RoleValue;
import com.zaranik.cursework.authservice.entities.User;
import com.zaranik.cursework.authservice.repositories.RoleRepository;
import com.zaranik.cursework.authservice.repositories.UserRepository;
import com.zaranik.cursework.authservice.services.hashingutility.HashingUtilityService;
import com.zaranik.cursework.authservice.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class RefreshTokenTest {

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  HashingUtilityService hashingUtilityService;

  @Autowired
  AuthService authService;

  private static final int TOKEN_WILL_NOT_EXPIRE = 1_000_000;
  private static final int TOKEN_ALREADY_EXPIRED = -1_000_000;

  @Value("${zaranik.app.jwtSecret}")
  String jwtSecret;

  @BeforeEach
  public void initTestCase() {
    Role studentRole = new Role(RoleValue.STUDENT);
    Role teacherRole = new Role(RoleValue.TEACHER);
    Role adminRole = new Role(RoleValue.ADMIN);
    roleRepository.save(studentRole);
    roleRepository.save(teacherRole);
    roleRepository.save(adminRole);

    User student = User.builder()
      .setUsername("student")
      .setPassword("12345")
      .setFirstName("John")
      .setLastName("Doe")
      .setActivated(true)
      .setEmail("student@gmail.com")
      .build();
    student.setRole(studentRole);

    userRepository.save(student);
  }

  @Test
  public void refreshSuccess_RegularCase() throws Exception {
    LoginUserDto dto = new LoginUserDto("student", "12345");
    TokenDto login = authService.login(dto);
    String jsonBody = objectMapper.writeValueAsString(login);

    mvc.perform(post("/api/auth/refresh-token")
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.accessToken").isString())
      .andExpect(jsonPath("$.refreshToken").isString());
  }

  @Test
  public void refreshSuccess_AccessTokenExpiredButValid() throws Exception {
    JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(
      jwtSecret,
      TOKEN_ALREADY_EXPIRED,
      TOKEN_WILL_NOT_EXPIRE
    );

    LoginUserDto dto = new LoginUserDto("student", "12345");
    TokenDto login = authService.login(dto);

    String expiredToken = jwtTokenUtil.generateAccessJwtToken("student", RoleValue.STUDENT);
    login.setAccessToken(expiredToken);

    String jsonBody = objectMapper.writeValueAsString(login);

    mvc.perform(post("/api/auth/refresh-token")
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.accessToken").isString())
      .andExpect(jsonPath("$.refreshToken").isString());
  }

  @Test
  public void refreshFailed_AccessTokenNotValid() throws Exception {
    LoginUserDto dto = new LoginUserDto("student", "12345");
    TokenDto login = authService.login(dto);

    String notValidToken = "dbfjad.fjkdbjgbfs.fndsojkgbfidngoik";
    login.setAccessToken(notValidToken);

    String jsonBody = objectMapper.writeValueAsString(login);

    mvc.perform(post("/api/auth/refresh-token")
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnauthorized());
  }


  @Test
  public void refreshFailed_RefreshTokenExpired() throws Exception {
    JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(
      jwtSecret,
      TOKEN_ALREADY_EXPIRED,
      TOKEN_ALREADY_EXPIRED
    );

    LoginUserDto dto = new LoginUserDto("student", "12345");
    TokenDto login = authService.login(dto);

    String expiredToken = jwtTokenUtil.generateRefreshJwtToken("student", RoleValue.STUDENT);
    login.setRefreshToken(expiredToken);

    String jsonBody = objectMapper.writeValueAsString(login);

    mvc.perform(post("/api/auth/refresh-token")
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnauthorized());
  }

  @Test
  public void refreshFailed_RefreshTokenIsNotAsInDb() throws Exception {
    LoginUserDto dto = new LoginUserDto("student", "12345");
    TokenDto login = authService.login(dto);

    String notValidToken = "dbfjad.fjkdbjgbfs.fndsojkgbfidngoik";
    login.setAccessToken(notValidToken);

    String jsonBody = objectMapper.writeValueAsString(login);

    mvc.perform(post("/api/auth/refresh-token")
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnauthorized());
  }

}
