package com.zaranik.cursework.authservice.controllers;

import com.zaranik.cursework.authservice.dto.LoginUserDto;
import com.zaranik.cursework.authservice.dto.TokenDto;
import com.zaranik.cursework.authservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public TokenDto login(@Valid @RequestBody LoginUserDto registrationUserDto) {
    return authService.login(registrationUserDto);
  }

  @PostMapping("/logout")
  public void logout(@Valid @RequestBody TokenDto tokenDto) {
    authService.logout(tokenDto);
  }

  @PostMapping("/refresh-token")
  public TokenDto getNewAccessToken(@Valid @RequestBody TokenDto tokenDto) {
    return authService.refreshToken(tokenDto);
  }

  @PostMapping("/validate-token")
  public void checkIfTokenIsValid(@Valid @RequestBody TokenDto tokenDto) {
    authService.validateToken(tokenDto);
  }

}
