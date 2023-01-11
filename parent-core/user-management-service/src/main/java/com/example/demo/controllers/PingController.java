package com.example.demo.controllers;

import com.example.demo.dto.ResponseStringWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-management")
public class PingController {

  @GetMapping(value = "/ping", produces = "application/json")
  public ResponseStringWrapper getPong() {
    return ResponseStringWrapper.of("user-detail-service:pong");
  }

}
