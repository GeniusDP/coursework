package com.zaranik.coursework.checkerservice.services;

import com.zaranik.coursework.checkerservice.dtos.response.RateLimitDto;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

  private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

  public RateLimitDto getRateLimitDto(String username) {
    Bucket tokenBucket = resolveBucket(username);
    ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);
    if (probe.isConsumed()) {
      return new RateLimitDto(probe.getRemainingTokens(), 0L, true);
    }
    return new RateLimitDto(0L, probe.getNanosToWaitForRefill() / 1_000_000_000, false);
  }

  private Bucket resolveBucket(String username) {
    return cache.computeIfAbsent(username, this::createNewBucket);
  }

  private Bucket createNewBucket(String username) {
    return Bucket.builder()
      .addLimit(getBandwidth())
      .build();
  }

  @Lookup
  public Bandwidth getBandwidth() {
    return null;
  }

}
