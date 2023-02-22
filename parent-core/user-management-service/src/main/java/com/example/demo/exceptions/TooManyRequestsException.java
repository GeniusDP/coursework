package com.example.demo.exceptions;

import com.example.demo.dto.RateLimitDto;
import lombok.Data;

@Data
public class TooManyRequestsException extends RuntimeException {
  private transient final RateLimitDto rateLimitDto;

}
