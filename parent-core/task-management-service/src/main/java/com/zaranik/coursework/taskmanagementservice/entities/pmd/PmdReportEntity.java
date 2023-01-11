package com.zaranik.coursework.taskmanagementservice.entities.pmd;

import com.zaranik.coursework.taskmanagementservice.entities.BaseEntity;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "pmd_reports")
@NoArgsConstructor
@AllArgsConstructor
public class PmdReportEntity extends BaseEntity {

  @OneToMany(mappedBy = "pmdReportEntity")
  private List<PmdReportSourceFile> pmdReportSourceFiles;

}
