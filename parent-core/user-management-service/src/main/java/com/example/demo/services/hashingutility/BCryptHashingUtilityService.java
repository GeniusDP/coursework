package com.example.demo.services.hashingutility;

import java.util.Objects;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("default")
public class BCryptHashingUtilityService implements HashingUtilityService{

  @Override
  public boolean match(String hashedValue, String actualValue) {
    return Objects.equals(hashedValue, actualValue);
  }

  @Override
  public String hash(String value) {
    return value;
  }
}
