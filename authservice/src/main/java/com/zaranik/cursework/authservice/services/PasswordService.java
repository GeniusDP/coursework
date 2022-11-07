package com.zaranik.cursework.authservice.services;

import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    public boolean match(String hashedPassword, String password){
        return hashedPassword.equals(password);
    }

}
