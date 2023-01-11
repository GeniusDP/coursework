package com.zaranik.coursework.checkerservice.repositories;

import com.zaranik.coursework.checkerservice.entities.pmd.PmdViolation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PmdViolationRepository extends JpaRepository<PmdViolation, Long> {

}
