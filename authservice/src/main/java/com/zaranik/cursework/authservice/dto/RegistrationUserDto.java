package com.zaranik.cursework.authservice.dto;

import javax.validation.constraints.NotBlank;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationUserDto {

  @NotNull(message = "username must not be null")
  @Size(min=3, max = 50, message="username size should be from 3 to 50")
  private String username;

  @NotNull(message = "email must not be null")
  @Size(max = 50, message = "email max size = 50")
  @Email(message = "email must be a valid email")
  private String email;

  @NotNull(message = "password must not be null")
  @Size(min=5, max = 50, message = "password size should be from 5 to 50")
  private String password;

  @NotNull(message = "firstName must not be null")
  @Size(min = 1, max = 50, message = "firstName size should be from 1 to 50")
  private String firstName;

  @NotNull(message = "lastName must not be null")
  @Size(min = 1, max = 50, message = "lastName size should be from 1 to 50")
  private String lastName;

}
