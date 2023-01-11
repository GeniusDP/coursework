package com.zaranik.coursework.checkerservice.aspect.security.basic;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(0)
public class SimpleSecurityAspect {

  @Value("${auth-service.url}")
  private String authServiceUrl;

  @Autowired
  private AuthorizeChecker authorizeChecker;

  @Around("com.zaranik.coursework.checkerservice.aspect.security.SecurityPointcuts.aroundSecuredMethod()")
  public Object allSecuredMethodsAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
    String url = authServiceUrl + "/validate-token";
    return authorizeChecker.validateTokens(joinPoint, url);
  }

}
