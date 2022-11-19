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
import com.zaranik.cursework.authservice.services.hashingutility.HashingUtilityService;
import com.zaranik.cursework.authservice.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtTokenUtil jwtTokenUtil;
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final HashingUtilityService hashingUtilityService;

  public TokenDto login(LoginUserDto loginUserDto) {
    String username = loginUserDto.getUsername();
    String password = loginUserDto.getPassword();

    if (!userRepository.existsByUsername(username)) {
      throw new LoginException();
    }

    User user = userRepository.findByUsername(username);
    if (!user.isActivated()) {
      throw new UserNotActivatedException();
    }

    if (!hashingUtilityService.match(user.getPassword(), password)) {
      throw new LoginException();
    }

    RoleValue role = user.getRole().getName();

    String refreshJwtToken = jwtTokenUtil.generateRefreshJwtToken(username, role);
    user.setRefreshToken(refreshJwtToken);
    userRepository.save(user);

    String accessJwtToken = jwtTokenUtil.generateAccessJwtToken(username, role);
    return new TokenDto(accessJwtToken, refreshJwtToken);
  }

  public TokenDto refreshToken(@Valid TokenDto tokenDto) {
    String accessToken = tokenDto.getAccessToken();
    String username = jwtTokenUtil.safeGetUserNameFromProbablyExpiredJwtToken(accessToken);
    RoleValue roleValue = RoleValue.valueOf(
        jwtTokenUtil.safeGetRoleNameFromProbablyExpiredToken(accessToken));

    if (!userRepository.existsByUsername(username)) {
      throw new AccessTokenInvalidException();
    }

    User user = userRepository.findByUsername(username);

    String providedRefreshToken = tokenDto.getRefreshToken();

    if (!hashingUtilityService.match(user.getRefreshToken(), providedRefreshToken)) {
      throw new RefreshTokenInvalidException();
    }

    if (!jwtTokenUtil.tokenIsValid(providedRefreshToken)) {
      throw new RefreshTokenInvalidException();
    }

    String accessJwtToken = jwtTokenUtil.generateAccessJwtToken(username, roleValue);
    String refreshJwtToken = jwtTokenUtil.generateRefreshJwtToken(username, roleValue);

    user.setRefreshToken(hashingUtilityService.hash(refreshJwtToken));
    userRepository.save(user);

    return new TokenDto(accessJwtToken, refreshJwtToken);
  }

  public void validateToken(@Valid TokenDto tokenDto) {
    String accessToken = tokenDto.getAccessToken();
    if (!jwtTokenUtil.tokenIsValid(accessToken)) {
      throw new AccessTokenInvalidException();
    }
  }

  public void logout(@Valid TokenDto tokenDto) {
    String accessToken = tokenDto.getAccessToken();

    if (!jwtTokenUtil.tokenIsValid(accessToken)) {
      throw new AccessTokenInvalidException();
    }
    String username = jwtTokenUtil.safeGetUserNameFromProbablyExpiredJwtToken(accessToken);

    User user = userRepository.findByUsername(username);
    user.setRefreshToken(null);
    userRepository.save(user);
  }

}
