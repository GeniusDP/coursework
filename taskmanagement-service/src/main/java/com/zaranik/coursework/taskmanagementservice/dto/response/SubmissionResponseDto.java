package com.zaranik.coursework.taskmanagementservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SubmissionResponseDto {
  private Long id;
  private String compilationStatus;
  private Integer testsNumber;
  private Integer testsPassed;
  private String testingStatus;
  private Long userId;
}
