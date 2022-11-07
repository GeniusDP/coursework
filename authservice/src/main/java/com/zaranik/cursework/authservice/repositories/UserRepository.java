package com.zaranik.cursework.authservice.repositories;

import com.zaranik.cursework.authservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    User findByUsername(String username);

}

