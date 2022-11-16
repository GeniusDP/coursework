package com.example.demo.aspect.security.basic;

import com.example.demo.dto.TokenContainingDto;
import com.example.demo.exceptions.AuthServiceUnreachableException;
import com.example.demo.exceptions.UnauthorizedException;
import java.util.Arrays;
import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthorizeChecker {

  public Object validateTokens(ProceedingJoinPoint joinPoint, String url) throws Throwable {
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

    try {
      restTemplate.postForEntity(url, tokenDto, Object.class);
    } catch (RestClientException e) {
      if (e instanceof HttpClientErrorException) {
        throw new UnauthorizedException();
      }
      throw new AuthServiceUnreachableException();
    }
    return joinPoint.proceed();

  }
}
