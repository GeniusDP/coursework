package com.zaranik.coursework.checkerservice.aspect.security.roles.teacher;

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
public class CheckTeacherGrantAspect {

  private final RoleChecker roleChecker;

  @Around("com.zaranik.coursework.checkerservice.aspect.security.SecurityPointcuts.teacherGrantAround()")
  public Object aroundMethodWithTeacher(ProceedingJoinPoint joinPoint) {
    return roleChecker.checkRole(joinPoint, RoleValue.TEACHER);
  }

}
