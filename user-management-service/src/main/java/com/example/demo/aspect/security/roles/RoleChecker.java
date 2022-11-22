package com.example.demo.aspect.security.roles;

import com.example.demo.entities.RoleValue;
import com.example.demo.exceptions.ForbiddenAccessException;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.utils.JwtTokenUtil;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleChecker {

  private final JwtTokenUtil jwtTokenUtil;
  private final HttpServletRequest httpServletRequest;

  public Object checkRole(ProceedingJoinPoint joinPoint, RoleValue roleValue){
    String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
    if(authHeader == null || !authHeader.matches("^Bearer\s.*$")){
      throw new UnauthorizedException();
    }
    try {
      String accessToken = authHeader.substring(7);
      String tokenRoleName = jwtTokenUtil.getRoleNameFromToken(accessToken);
      if(RoleValue.getRoleValue(tokenRoleName) != roleValue){
        throw new ForbiddenAccessException();
      }
      return joinPoint.proceed();
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

}
