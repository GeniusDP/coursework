package com.zaranik.coursework.taskmanagementservice.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class TaskCreationDto {
  @NotNull private String name;
  @NotNull private String description;
  @NotNull private MultipartFile sourceInZip;
  @NotNull private MultipartFile testSourceInZip;
  @NotNull private Boolean checkstyleNeeded;
  @NotNull private Boolean pmdNeeded;
  @NotNull private Integer pmdPoints;
  @NotNull private Integer checkstylePoints;
  @NotNull private Integer testPoints;
  @NotNull @Min(-1) private Integer submissionsNumberLimit;
}
