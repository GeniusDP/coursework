package com.zaranik.cursework.authservice.services.hashingutility;

public interface HashingUtilityService {

  boolean match(String hashedValue, String actualValue);

  String hash(String value);
}
