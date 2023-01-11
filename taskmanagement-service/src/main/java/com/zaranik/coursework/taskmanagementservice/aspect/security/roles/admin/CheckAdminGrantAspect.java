package com.zaranik.coursework.taskmanagementservice.aspect.security.roles.admin;

import com.zaranik.coursework.taskmanagementservice.aspect.security.roles.RoleChecker;
import com.zaranik.coursework.taskmanagementservice.dto.RoleValue;
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

  @Around("com.zaranik.coursework.taskmanagementservice.aspect.security.SecurityPointcuts.adminGrantAround()")
  public Object aroundMethodWithAdminGrant(ProceedingJoinPoint joinPoint) {
    return roleChecker.checkRole(joinPoint, RoleValue.ADMIN);
  }

}
