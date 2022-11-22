package com.example.demo.aspect.security;

import org.aspectj.lang.annotation.Pointcut;

public class SecurityPointcuts {

  @Pointcut("@annotation(com.example.demo.aspect.security.basic.SecuredRoute)")
  public void aroundSecuredMethod() {
  }

  @Pointcut("@annotation(com.example.demo.aspect.security.roles.teacher.TeacherGrant)")
  public void teacherGrantAround() {
  }

  @Pointcut("@annotation(com.example.demo.aspect.security.roles.admin.AdminGrant)")
  public void adminGrantAround() {
  }

}
