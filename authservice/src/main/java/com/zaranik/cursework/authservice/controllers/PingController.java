package com.zaranik.cursework.authservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class PingController {

    @GetMapping(value = "/ping", produces = "application/json")
    public String getPong(){
        return "auth:pong";
    }

}
