package com.example.demo.repositories;

import com.example.demo.entities.Role;
import com.example.demo.entities.RoleValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(RoleValue name);
}
