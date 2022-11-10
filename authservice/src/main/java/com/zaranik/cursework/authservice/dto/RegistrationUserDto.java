package com.zaranik.cursework.authservice.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationUserDto {

  @NotNull
  @Size(max = 50)
  private String username;

  @NotNull
  @Size(max = 50)
  @Email
  private String email;

  @NotNull
  @Size(max = 50)
  private String password;

  @NotNull
  @Size(max = 50)
  private String firstName;

  @NotNull
  @Size(max = 50)
  private String lastName;

}
