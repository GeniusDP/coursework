package com.zaranik.cursework.authservice.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto {

  @NotNull(message = "username must not be null")
  @Size(min = 3, max = 50, message="username size should be from 3 to 50")
  private String username;

  @NotNull(message = "password must not be null")
  @Size(min = 5, max = 50, message = "password size should be from 5 to 50")
  private String password;

}
