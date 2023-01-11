package com.zaranik.cursework.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStringWrapper {

  private String value;

  public static ResponseStringWrapper of(String value) {
    return new ResponseStringWrapper(value);
  }
}
