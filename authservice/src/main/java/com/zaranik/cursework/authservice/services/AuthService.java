package com.zaranik.cursework.authservice.services;

import com.zaranik.cursework.authservice.dto.LoginUserDto;
import com.zaranik.cursework.authservice.dto.TokenDto;
import com.zaranik.cursework.authservice.dto.RegistrationUserDto;
import com.zaranik.cursework.authservice.exceptions.*;
import com.zaranik.cursework.authservice.entities.Role;
import com.zaranik.cursework.authservice.entities.RoleValue;
import com.zaranik.cursework.authservice.entities.User;
import com.zaranik.cursework.authservice.repositories.RoleRepository;
import com.zaranik.cursework.authservice.repositories.UserRepository;
import com.zaranik.cursework.authservice.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public TokenDto login(LoginUserDto loginUserDto) {
        String username = loginUserDto.getUsername();
        String password = loginUserDto.getPassword();

        if (!userRepository.existsByUsername(username)) {
            throw new LoginException();
        }

        User user = userRepository.findByUsername(username);
        if (!passwordService.match(user.getPassword(), password)) {
            throw new LoginException();
        }
        String refreshJwtToken = jwtTokenUtil.generateRefreshJwtToken(username);
        user.setRefreshToken(refreshJwtToken);
        userRepository.save(user);

        String accessJwtToken = jwtTokenUtil.generateAccessJwtToken(username);
        return new TokenDto(accessJwtToken);
    }

    @Transactional
    public void register(RegistrationUserDto registrationUserDto) {
        String username = registrationUserDto.getUsername();
        String password = registrationUserDto.getPassword();
        String email = registrationUserDto.getEmail();
        String firstName = registrationUserDto.getFirstName();
        String lastName = registrationUserDto.getLastName();
        if (userRepository.existsByUsername(username)) {
            throw new RegistrationException("User with such username already exists");
        }
        Role userRole = roleRepository.findByName(RoleValue.USER);
        User newUser = User.builder()
                .setUsername(username)
                .setPassword(password)
                .setEmail(email)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setActivated(false)
                .build();
        newUser.setRole(userRole);
        userRepository.save(newUser);
    }

    public TokenDto refreshToken(String authHeader) {
        String token = getTokenFromAuthHeader(authHeader);
        String username = jwtTokenUtil.safeGetUserNameFromProbablyExpiredJwtToken(token);
        if(!userRepository.existsByUsername(username)){
            throw new AccessTokenInvalidException();
        }
        String refreshToken = userRepository.findByUsername(username).getRefreshToken();
        if (!jwtTokenUtil.tokenIsValid(refreshToken)) {
            throw new RefreshTokenInvalidException();
        }
        String accessJwtToken = jwtTokenUtil.generateAccessJwtToken(username);
        return new TokenDto(accessJwtToken);
    }

    public void validateToken(String authHeader) {
        String token = getTokenFromAuthHeader(authHeader);
        if (!jwtTokenUtil.tokenIsValid(token)) {
            throw new AccessTokenInvalidException();
        }
    }

    @Transactional
    public void logout(String authHeader) {
        String token = getTokenFromAuthHeader(authHeader);
        String username = jwtTokenUtil.safeGetUserNameFromProbablyExpiredJwtToken(token);

        if(!userRepository.existsByUsername(username)){
            throw new AccessTokenInvalidException();
        }

        User user = userRepository.findByUsername(username);
        user.setRefreshToken(null);
        userRepository.save(user);
    }

    private String getTokenFromAuthHeader(String authHeader){
        if(authHeader == null || authHeader.length() < 7){
            throw new AccessTokenInvalidException("Not a token provided in Authorization request header");
        }
        return authHeader.substring(7);
    }

    public RoleValue getUserRole(String authHeader) {
        validateToken(authHeader);
        String token = getTokenFromAuthHeader(authHeader);
        String username = jwtTokenUtil.safeGetUserNameFromProbablyExpiredJwtToken(token);

        if(!userRepository.existsByUsername(username)){
            throw new AccessTokenInvalidException();
        }

        User user = userRepository.findByUsername(username);
        return user.getRole().getName();
    }
}
