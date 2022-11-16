package com.example.demo.aspect.security.roles.admin;

import com.example.demo.aspect.security.roles.RoleChecker;
import com.example.demo.entities.RoleValue;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(-1)
@RequiredArgsConstructor
public class CheckAdminGrantAspect {

  private final RoleChecker roleChecker;

  @Around("com.example.demo.aspect.security.SecurityPointcuts.adminGrantAround()")
  public Object aroundMethodWithAdminGrant(ProceedingJoinPoint joinPoint) {
    return roleChecker.checkRole(joinPoint, RoleValue.ADMIN);
  }

}
