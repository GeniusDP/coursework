package com.zaranik.coursework.checkerservice.aspect.security;

import org.aspectj.lang.annotation.Pointcut;

public class SecurityPointcuts {

  @Pointcut("@annotation(com.zaranik.coursework.checkerservice.aspect.security.basic.SecuredRoute)")
  public void aroundSecuredMethod() {
  }

  @Pointcut("@annotation(com.zaranik.coursework.checkerservice.aspect.security.roles.teacher.TeacherGrant)")
  public void teacherGrantAround() {
  }

  @Pointcut("@annotation(com.zaranik.coursework.checkerservice.aspect.security.roles.admin.AdminGrant)")
  public void adminGrantAround() {
  }

}
