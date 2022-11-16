package com.example.demo.aspect.security.basic;

import com.example.demo.dto.TokenContainingDto;
import com.example.demo.exceptions.AuthServiceUnreachableException;
import com.example.demo.exceptions.UnauthorizedException;
import java.util.Arrays;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Aspect
@Component
@Order(0)
public class SimpleSecurityAspect {

  @Value("${auth-service.url}")
  private String authServiceUrl;

  @Around("com.example.demo.aspect.security.SecurityPointcuts.aroundSecuredMethod()")
  public Object allSecuredMethodsAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
    Object[] args = joinPoint.getArgs();

    Optional<TokenContainingDto> tokenDtoOptional = Arrays.stream(args)
        .filter(arg -> arg instanceof TokenContainingDto)
        .map(arg -> (TokenContainingDto) arg)
        .findFirst();

    if (tokenDtoOptional.isEmpty()) {
      throw new UnauthorizedException();
    }

    TokenContainingDto tokenDto = tokenDtoOptional.get();
    RestTemplate restTemplate = new RestTemplate();
    String url = authServiceUrl + "/validate-token";

    try {
      ResponseEntity<?> entity = restTemplate.postForEntity(url, tokenDto, Object.class);
    } catch (RestClientException e) {
      if (e instanceof HttpClientErrorException) {
        throw new UnauthorizedException();
      }
      throw new AuthServiceUnreachableException();
    }
    return joinPoint.proceed();
  }


}
