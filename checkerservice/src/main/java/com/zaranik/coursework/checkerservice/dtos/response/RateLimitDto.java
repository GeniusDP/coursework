package com.zaranik.coursework.checkerservice.dtos.response;

public record RateLimitDto(
  Long limitRemaining,
  Long retryAfterSeconds,
  Boolean succeeded
) {}
