package com.zaranik.cursework.authservice.services;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class HashingUtilityService {

  public boolean match(String hashedValue, String actualValue) {
    return Objects.equals(hashedValue, actualValue);
  }

  public String hash(String value) {
    return value;
  }

}
