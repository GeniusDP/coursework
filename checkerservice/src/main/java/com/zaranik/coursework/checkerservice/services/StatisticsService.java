package com.zaranik.coursework.checkerservice.services;

import com.zaranik.coursework.checkerservice.dtos.response.DoubleNumberValueDto;
import com.zaranik.coursework.checkerservice.dtos.response.LongNumberValueDto;
import com.zaranik.coursework.checkerservice.dtos.response.RuntimeStatusStatsDto;
import com.zaranik.coursework.checkerservice.dtos.response.TotalScoreDistributionStatsDto;
import com.zaranik.coursework.checkerservice.entities.RuntimeStatus;
import com.zaranik.coursework.checkerservice.repositories.SolutionRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatisticsService {

  private final SolutionRepository repository;

  public List<RuntimeStatusStatsDto> countRuntimeStatusesGrouped(Long taskId) {
    List<RuntimeStatusStatsDto> list = repository.countRuntimeStatusesGrouped(taskId);
    Set<RuntimeStatus> runtimeStatuses = list.stream().map(RuntimeStatusStatsDto::status).collect(Collectors.toSet());
    for (RuntimeStatus status : RuntimeStatus.values()) {
      if( !runtimeStatuses.contains(status) ) {
        list.add(new RuntimeStatusStatsDto(status, 0L));
      }
    }
    return list;
  }


  public DoubleNumberValueDto countPercentageOfFullyCorrectSolutions(Long taskId) {
    int all = repository.countAllByTaskId(taskId);
    log.info("all = {}", all);
    int fullyCompleted = repository.countAllByTaskIdAndTotalScoreIsGreaterThanEqual(taskId, 100.);
    log.info("fullyCompleted = {}", fullyCompleted);
    if(all == 0){
      return new DoubleNumberValueDto(0., "no submissions on this task yet");
    }
    return new DoubleNumberValueDto((fullyCompleted * 10000) / all / 10000. * 100, "percentage");
  }

  public TotalScoreDistributionStatsDto getDistributionOfTotalScores(Long taskId) {
    return new TotalScoreDistributionStatsDto(
      repository.countAllByTaskIdAndTotalScoreIsGreaterThanEqualAndTotalScoreIsLessThanEqual(taskId, 0., 20.),
      repository.countAllByTaskIdAndTotalScoreIsGreaterThanEqualAndTotalScoreIsLessThanEqual(taskId, 21., 40.),
      repository.countAllByTaskIdAndTotalScoreIsGreaterThanEqualAndTotalScoreIsLessThanEqual(taskId, 41., 60.),
      repository.countAllByTaskIdAndTotalScoreIsGreaterThanEqualAndTotalScoreIsLessThanEqual(taskId, 61., 80.),
      repository.countAllByTaskIdAndTotalScoreIsGreaterThanEqualAndTotalScoreIsLessThanEqual(taskId, 81., 100.),
      repository.countAllByTaskIdAndTotalScoreIsNotNull(taskId)
    );
  }

  public DoubleNumberValueDto getAverageTotalScore(Long taskId) {
    int all = repository.countAllByTaskIdAndTotalScoreIsNotNull(taskId);
    log.info("all = {}", all);
    if(all == 0) {
      return new DoubleNumberValueDto(0., "no submissions on this task yet");
    }
    log.info("sum = {}", repository.sumOfAllNonNullByTaskId(taskId));
    double avgTotalScore = repository.sumOfAllNonNullByTaskId(taskId) / all;
    log.info("sum = {}", repository.sumOfAllNonNullByTaskId(taskId));
    return new DoubleNumberValueDto(avgTotalScore, "average total score");
  }

  public LongNumberValueDto countSubmissionsNumberOfTask(Long taskId) {
    long all = repository.countAllByTaskId(taskId);
    return new LongNumberValueDto(all, "average total score");
  }

}
