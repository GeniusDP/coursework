package com.example.demo.dto;

public record RateLimitDto(
  Long limitRemaining,
  Long retryAfterSeconds,
  Boolean succeeded
) {}

