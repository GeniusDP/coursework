package com.zaranik.coursework.checkerservice.repositories;

import com.zaranik.coursework.checkerservice.dtos.RuntimeStatusStatsDto;
import com.zaranik.coursework.checkerservice.entities.Solution;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SolutionRepository extends JpaRepository<Solution, Long> {

  List<Solution> findAllByUserUsernameIsAndTaskIdIs(String userUsername, Long taskId);

  Optional<Solution> findSolutionByIdAndUserUsername(Long solutionId, String username);

  List<Solution> findAllByTaskId(Long taskId);

  Optional<Solution> findSolutionById(Long submissionId);

  @Transactional
  @Query("""
        select new com.zaranik.coursework.checkerservice.dtos.RuntimeStatusStatsDto(
          s.runtimeStatus,
          count(s.runtimeStatus)
        ) from Solution s group by s.runtimeStatus
    """)
  List<RuntimeStatusStatsDto> countRuntimeStatusesGrouped();
}
