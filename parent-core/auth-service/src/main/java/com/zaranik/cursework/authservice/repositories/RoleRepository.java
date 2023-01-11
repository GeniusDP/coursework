package com.zaranik.cursework.authservice.repositories;

import com.zaranik.cursework.authservice.entities.Role;
import com.zaranik.cursework.authservice.entities.RoleValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(RoleValue name);
}
