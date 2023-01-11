package com.example.demo.services.hashingutility;

public interface HashingUtilityService {

  boolean match(String hashedValue, String actualValue);

  String hash(String value);
}
