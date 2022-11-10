package com.zaranik.cursework.authservice.controllers;

import com.zaranik.cursework.authservice.dto.LoginUserDto;
import com.zaranik.cursework.authservice.dto.RegistrationUserDto;
import com.zaranik.cursework.authservice.dto.RoleDto;
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
    public void logout(@RequestHeader("Authorization") String authHeader) {
        authService.logout(authHeader);
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        authService.register(registrationUserDto);
    }

    @GetMapping("/refresh-token")
    public TokenDto getNewAccessToken(@RequestHeader("Authorization") String authHeader) {
        return authService.refreshToken(authHeader);
    }

    @GetMapping("/validate-token")
    public void checkIfTokenIsValid(@RequestHeader("Authorization") String authHeader) {
        authService.validateToken(authHeader);
    }

    @GetMapping("/get-role")
    public RoleDto getUserRole(@RequestHeader("Authorization") String authHeader) {
        return new RoleDto(authService.getUserRole(authHeader));
    }

}
