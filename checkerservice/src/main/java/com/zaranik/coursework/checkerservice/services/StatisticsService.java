package com.zaranik.coursework.checkerservice.services;

import com.zaranik.coursework.checkerservice.dtos.RuntimeStatusStatsDto;
import com.zaranik.coursework.checkerservice.entities.RuntimeStatus;
import com.zaranik.coursework.checkerservice.repositories.SolutionRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService {

  private final SolutionRepository repository;

  public List<RuntimeStatusStatsDto> countRuntimeStatusesGrouped() {
    List<RuntimeStatusStatsDto> list = repository.countRuntimeStatusesGrouped();
    Set<RuntimeStatus> runtimeStatuses = list.stream().map(RuntimeStatusStatsDto::status).collect(Collectors.toSet());
    for (RuntimeStatus status : RuntimeStatus.values()) {
      if( !runtimeStatuses.contains(status) ) {
        list.add(new RuntimeStatusStatsDto(status, 0L));
      }
    }
    return list;
  }

}
