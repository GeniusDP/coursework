package com.zaranik.coursework.checkerservice.repositories;

import com.zaranik.coursework.checkerservice.entities.checkstyle.CheckstyleError;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckstyleErrorRepository extends JpaRepository<CheckstyleError, Long> {

}
