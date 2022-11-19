package com.zaranik.cursework.authservice.services;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaranik.cursework.authservice.dto.LoginUserDto;
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
class LoginTest {

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

    User studentNotActivated = User.builder()
      .setUsername("student_not_activated")
      .setPassword("12345")
      .setFirstName("John2")
      .setLastName("Doe2")
      .setActivated(false)
      .setEmail("student2@gmail.com")
      .build();
    studentNotActivated.setRole(studentRole);

    User userLoggedIn = User.builder()
      .setUsername("userLoggedIn")
      .setPassword("12345")
      .setFirstName("John3")
      .setLastName("Doe3")
      .setActivated(true)
      .setEmail("student3@gmail.com")
      .build();
    studentNotActivated.setRole(studentRole);

    userRepository.save(student);
    userRepository.save(studentNotActivated);
    userRepository.save(userLoggedIn);
  }

  @Test
  void loginSuccess() throws Exception {
    LoginUserDto dto = new LoginUserDto("student", "12345");

    String jsonBody = objectMapper.writeValueAsString(dto);

    mvc.perform(post("/api/auth/login")
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.accessToken").isString())
      .andExpect(jsonPath("$.refreshToken").isString());
  }

  @Test
  void loginFailure_UsernameWrong() throws Exception {
    LoginUserDto dto = new LoginUserDto("wrongStudentName", "some_password");

    String jsonBody = objectMapper.writeValueAsString(dto);

    mvc.perform(post("/api/auth/login")
        .content(jsonBody).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  void loginFailure_LoginOk_But_PasswordWrong() throws Exception {
    LoginUserDto dto = new LoginUserDto("student", "some_wrong_password");

    String jsonBody = objectMapper.writeValueAsString(dto);

    mvc.perform(post("/api/auth/login")
        .content(jsonBody).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }


  @Test
  void loginFailure_NotActivated() throws Exception {
    LoginUserDto dto = new LoginUserDto("studentNotActivated", "some_password");

    String jsonBody = objectMapper.writeValueAsString(dto);

    mvc.perform(post("/api/auth/login")
        .content(jsonBody).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

}