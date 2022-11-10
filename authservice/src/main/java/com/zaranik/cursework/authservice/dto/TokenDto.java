package com.zaranik.cursework.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

  @NotNull
  private String accessToken;

  @NotNull
  private String refreshToken;

}
