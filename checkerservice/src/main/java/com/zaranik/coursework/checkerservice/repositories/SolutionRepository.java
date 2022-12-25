package com.zaranik.coursework.checkerservice.repositories;

import com.zaranik.coursework.checkerservice.entities.Solution;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SolutionRepository extends JpaRepository<Solution, Long> {

  List<Solution> findAllByUserUsernameIsAndTaskIdIs(String userUsername, Long taskId);

  Optional<Solution> findSolutionByIdAndUserUsername(Long solutionId, String username);

  List<Solution> findAllByTaskId(Long taskId);

  Optional<Solution> findSolutionById(Long submissionId);
}
