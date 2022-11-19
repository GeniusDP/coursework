package com.example.demo.aspect.security.roles;

import com.example.demo.dto.AccessTokenInvalidException;
import com.example.demo.dto.TokenContainingDto;
import com.example.demo.entities.RoleValue;
import com.example.demo.exceptions.ForbiddenAccessException;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.utils.JwtTokenUtil;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleChecker {

  private final JwtTokenUtil jwtTokenUtil;

  public Object checkRole(ProceedingJoinPoint joinPoint, RoleValue roleValue){
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
      if(RoleValue.valueOf(roleName) != roleValue){
        throw new ForbiddenAccessException();
      }
      return joinPoint.proceed();
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

}
