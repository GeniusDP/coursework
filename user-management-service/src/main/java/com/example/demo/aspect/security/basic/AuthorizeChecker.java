package com.example.demo.aspect.security.basic;

import com.example.demo.exceptions.AuthServiceUnreachableException;
import com.example.demo.exceptions.UnauthorizedException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AuthorizeChecker {

  private final HttpServletRequest httpServletRequest;

  public Object validateTokens(ProceedingJoinPoint joinPoint, String url) throws Throwable {
    String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
    if(authHeader == null || !authHeader.matches("^Bearer\s.*$")){
      throw new UnauthorizedException();
    }

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, authHeader);

    HttpEntity<?> requestEntity = new HttpEntity<>(headers);

    try {
      restTemplate.exchange(url, HttpMethod.GET, requestEntity, Object.class);
    } catch (RestClientException e) {
      if (e instanceof HttpClientErrorException) {
        throw new UnauthorizedException();
      }
      throw new AuthServiceUnreachableException();
    }
    return joinPoint.proceed();

  }
}
