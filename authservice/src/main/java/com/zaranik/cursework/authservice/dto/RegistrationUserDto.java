package com.zaranik.cursework.authservice.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@RequiredArgsConstructor
public class RegistrationUserDto {

    @NotNull
    @Size(max = 50)
    private final String username;

    @NotNull
    @Size(max = 50)
    @Email
    private final String email;

    @NotNull
    @Size(max = 50)
    private final String password;

    @NotNull
    @Size(max = 50)
    private final String firstName;

    @NotNull
    @Size(max = 50)
    private final String lastName;
    
}
