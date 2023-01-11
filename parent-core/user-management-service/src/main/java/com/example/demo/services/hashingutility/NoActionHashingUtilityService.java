package com.example.demo.services.hashingutility;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Profile({"dev", "test"})
public class NoActionHashingUtilityService implements HashingUtilityService {

  public boolean match(String hashedValue, String actualValue) {
    return Objects.equals(hashedValue, actualValue);
  }

  public String hash(String value) {
    return value;
  }

}
