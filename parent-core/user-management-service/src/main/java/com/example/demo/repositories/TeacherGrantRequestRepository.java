package com.example.demo.repositories;

import com.example.demo.entities.TeacherGrantRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherGrantRequestRepository extends JpaRepository<TeacherGrantRequest, Long> {

  void deleteAllByUsername(String username);

}
