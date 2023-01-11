package com.zaranik.coursework.taskmanagementservice.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TaskUpdateDto {
  @NotNull private String name;
  @NotNull private String description;
  @NotNull private MultipartFile sourceInZip;
  @NotNull private MultipartFile testSourceInZip;
  @NotNull @Min(-1) private Integer submissionsNumberLimit;
}
