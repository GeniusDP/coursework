package com.zaranik.coursework.checkerservice.repositories;

import com.zaranik.coursework.checkerservice.entities.Solution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionRepository extends JpaRepository<Solution, Long> {

}
