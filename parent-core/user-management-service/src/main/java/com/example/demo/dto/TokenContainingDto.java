package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenContainingDto {

  @NotNull(message = "must not be null")
  protected String accessToken;

  @NotNull(message = "must not be null")
  protected String refreshToken;

}
