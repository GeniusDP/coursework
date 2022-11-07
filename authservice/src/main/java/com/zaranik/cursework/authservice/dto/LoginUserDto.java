package com.zaranik.cursework.authservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@RequiredArgsConstructor
public class LoginUserDto {
    @NotNull
    @Size(max = 50)
    private final String username;

    @NotNull
    @Size(max = 50)
    private final String password;

}
