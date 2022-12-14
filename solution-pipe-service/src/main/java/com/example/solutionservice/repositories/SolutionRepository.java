package com.example.solutionservice.repositories;

import com.example.solutionservice.entities.Solution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionRepository extends JpaRepository<Solution, Long> {

}
