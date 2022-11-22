package com.zaranik.cursework.authservice.controllers;

import com.zaranik.cursework.authservice.dto.LoginUserDto;
import com.zaranik.cursework.authservice.dto.TokenDto;
import com.zaranik.cursework.authservice.services.AuthService;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public TokenDto login(@Valid @RequestBody LoginUserDto registrationUserDto) {
    return authService.login(registrationUserDto);
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  public void logout(@Valid @RequestBody TokenDto tokenDto) {
    authService.logout(tokenDto);
  }

  @PostMapping("/refresh-token")
  @ResponseStatus(HttpStatus.OK)
  public TokenDto getNewAccessToken(@Valid @RequestBody TokenDto tokenDto) {
    return authService.refreshToken(tokenDto);
  }

  @GetMapping("/validate-token")
  @ResponseStatus(HttpStatus.OK)
  public void checkIfTokenIsValid(@RequestHeader("Authorization") @Pattern(regexp = "^Bearer\s.*$") String accessToken) {
    String tokenWithoutBearerPrefix = accessToken.substring(7);
    authService.validateToken(tokenWithoutBearerPrefix);
  }

}
