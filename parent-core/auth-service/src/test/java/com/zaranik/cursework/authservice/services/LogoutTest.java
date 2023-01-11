package com.zaranik.cursework.authservice.services;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LogoutTest {

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
  public void logoutSuccess_RegularCase() throws Exception {
    LoginUserDto dto = new LoginUserDto("student", "12345");
    TokenDto login = authService.login(dto);
    String jsonBody = objectMapper.writeValueAsString(login);

    User student = userRepository.findByUsername("student");
    assertThat(student.getRefreshToken()).isNotNull();

    mvc.perform(post("/api/auth/logout")
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());

    student = userRepository.findByUsername("student");
    assertThat(student.getRefreshToken()).isNull();
  }

  @Test
  public void logoutFailed_AccessTokenInvalid() throws Exception {
    LoginUserDto dto = new LoginUserDto("student", "12345");
    TokenDto tokenDto = authService.login(dto);
    tokenDto.setAccessToken("some.tampered.value");
    String jsonBody = objectMapper.writeValueAsString(tokenDto);

    User student = userRepository.findByUsername("student");
    assertThat(student.getRefreshToken()).isNotNull();
    mvc.perform(post("/api/auth/logout")
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnauthorized());
    student = userRepository.findByUsername("student");
    assertThat(student.getRefreshToken()).isNotNull();
  }

  @Test
  public void logoutFailed_RefreshTokenIsNotTheSameAsInDatabase() throws Exception {
    LoginUserDto dto = new LoginUserDto("student", "12345");
    TokenDto tokenDto = authService.login(dto);
    tokenDto.setRefreshToken("some.tampered.value");
    String jsonBody = objectMapper.writeValueAsString(tokenDto);

    User student = userRepository.findByUsername("student");
    assertThat(student.getRefreshToken()).isNotNull();
    mvc.perform(post("/api/auth/logout")
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnauthorized());
    student = userRepository.findByUsername("student");
    assertThat(student.getRefreshToken()).isNotNull();
  }


}
