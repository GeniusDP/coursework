package com.example.solutionservice.services;

import com.example.solutionservice.dto.response.SolutionDto;
import com.example.solutionservice.entities.Solution;
import com.example.solutionservice.exceptions.SolutionNotFoundException;
import com.example.solutionservice.repositories.SolutionRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolutionService {

  private final SolutionRepository repository;

  public SolutionDto getSolutionById(Long id) {
    Optional<Solution> solutionById = repository.findById(id);
    if (solutionById.isEmpty()) {
      throw new SolutionNotFoundException();
    }
    Solution solution = solutionById.get();
    byte[] bytes = solution.getSourceInZip();
    return new SolutionDto(bytes);
  }
}
