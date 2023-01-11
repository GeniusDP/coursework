package com.zaranik.cursework.authservice.controllers;

import com.zaranik.cursework.authservice.dto.ResponseStringWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class PingController {

    @GetMapping(value = "/ping", produces = "application/json")
    public ResponseStringWrapper getPong(){
        return ResponseStringWrapper.of("auth:pong");
    }

}
