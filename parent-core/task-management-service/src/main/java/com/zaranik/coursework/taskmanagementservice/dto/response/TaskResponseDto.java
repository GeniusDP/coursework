package com.zaranik.coursework.taskmanagementservice.dto.response;

import com.zaranik.coursework.taskmanagementservice.entities.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TaskResponseDto {
  private Long id;
  private String name;
  private String description;
  private String creatorName;
  private boolean pmdNeeded;
  private boolean checkstyleNeeded;
  private int pmdPoints;
  private int checkstylePoints;
  private int testPoints;
  private Integer submissionsNumberLimit;

  public static TaskResponseDto getFromEntity(Task task) {
    return TaskResponseDto.builder()
      .id(task.getId())
      .name(task.getName())
      .description(task.getDescription())
      .creatorName(task.getCreatorName())
      .pmdPoints(task.getPmdPoints())
      .checkstylePoints(task.getCheckstylePoints())
      .testPoints(task.getTestPoints())
      .pmdNeeded(task.isPmdNeeded())
      .checkstyleNeeded(task.isCheckstyleNeeded())
      .submissionsNumberLimit(task.getSubmissionsNumberLimit())
      .build();
  }
}
