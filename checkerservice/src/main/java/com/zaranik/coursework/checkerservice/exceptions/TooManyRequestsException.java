package com.zaranik.coursework.checkerservice.exceptions;

import com.zaranik.coursework.checkerservice.dtos.response.RateLimitDto;
import lombok.Data;

@Data
public class TooManyRequestsException extends RuntimeException {
  private final RateLimitDto rateLimitDto;

}
