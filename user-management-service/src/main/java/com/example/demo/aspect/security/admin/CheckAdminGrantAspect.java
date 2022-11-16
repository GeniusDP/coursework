package com.example.demo.aspect.security.admin;

import com.example.demo.dto.AccessTokenInvalidException;
import com.example.demo.dto.TokenContainingDto;
import com.example.demo.entities.RoleValue;
import com.example.demo.exceptions.AuthServiceUnreachableException;
import com.example.demo.exceptions.ForbiddenAccessException;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.utils.JwtTokenUtil;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(-1)
@RequiredArgsConstructor
public class CheckAdminGrantAspect {

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Around("com.example.demo.aspect.security.SecurityPointcuts.aroundMethodWithAdminGrant()")
  public Object aroundMethodWithAdminGrant(ProceedingJoinPoint joinPoint) {
    Object[] args = joinPoint.getArgs();

    Optional<TokenContainingDto> tokenDtoOptional = Arrays.stream(args)
        .filter(arg -> arg instanceof TokenContainingDto)
        .map(arg -> (TokenContainingDto) arg)
        .findFirst();

    if (tokenDtoOptional.isEmpty()) {
      throw new UnauthorizedException();
    }

    TokenContainingDto tokenDto = tokenDtoOptional.get();

    try {
      String accessToken = tokenDto.getAccessToken();
      String roleName = jwtTokenUtil.safeGetRoleNameFromProbablyExpiredToken(accessToken);
      if(roleName == null){
        throw new AccessTokenInvalidException();
      }
      RoleValue roleValue = RoleValue.valueOf(roleName);
      if(roleValue != RoleValue.ADMIN){
        throw new ForbiddenAccessException();
      }
      return joinPoint.proceed();
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }


}
