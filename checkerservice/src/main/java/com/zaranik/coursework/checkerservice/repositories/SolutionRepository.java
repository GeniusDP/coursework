package com.zaranik.coursework.checkerservice.repositories;

import com.zaranik.coursework.checkerservice.dtos.response.RuntimeStatusStatsDto;
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
        select
          new com.zaranik.coursework.checkerservice.dtos.response.RuntimeStatusStatsDto(
            s.runtimeStatus,
            count(s.runtimeStatus)
          )
        from Solution s
        where s IN (select s from Solution s where s.task.id=:taskId)
        group by s.runtimeStatus
    """)
  List<RuntimeStatusStatsDto> countRuntimeStatusesGrouped(Long taskId);

  int countAllByTaskId(Long taskId);
  int countAllByTaskIdAndTotalScoreIsNotNull(Long taskId);

  int countAllByTaskIdAndTotalScoreIsGreaterThanEqual(Long taskId, Double totalScore);
  int countAllByTaskIdAndTotalScoreIsGreaterThanEqualAndTotalScoreIsLessThanEqual(
    Long taskId,
    Double totalScoreLowerBound,
    Double totalScoreUpperBound
  );

  @Transactional
  @Query("""
    select sum(s.totalScore) from Solution s where s.totalScore is not null and s.task.id=:taskId
  """)
  double sumOfAllNonNullByTaskId(Long taskId);
}
