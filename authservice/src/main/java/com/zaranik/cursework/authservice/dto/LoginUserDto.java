package com.zaranik.cursework.authservice.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto {

  @NotNull
  @Size(max = 50)
  private String username;

  @NotNull
  @Size(max = 50)
  private String password;

}
