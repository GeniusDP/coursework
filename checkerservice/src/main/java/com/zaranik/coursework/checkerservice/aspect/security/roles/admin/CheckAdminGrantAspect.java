package com.zaranik.coursework.checkerservice.aspect.security.roles.admin;

import com.zaranik.coursework.checkerservice.aspect.security.roles.RoleChecker;
import com.zaranik.coursework.checkerservice.dtos.RoleValue;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(-100)
@RequiredArgsConstructor
public class CheckAdminGrantAspect {

  private final RoleChecker roleChecker;

  @Around("com.zaranik.coursework.checkerservice.aspect.security.SecurityPointcuts.adminGrantAround()")
  public Object aroundMethodWithAdminGrant(ProceedingJoinPoint joinPoint) {
    return roleChecker.checkRole(joinPoint, RoleValue.ADMIN);
  }

}
