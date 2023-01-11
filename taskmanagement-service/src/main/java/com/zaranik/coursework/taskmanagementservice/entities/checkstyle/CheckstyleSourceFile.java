package com.zaranik.coursework.taskmanagementservice.entities.checkstyle;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.zaranik.coursework.taskmanagementservice.entities.BaseEntity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "checkstyle_files")
@NoArgsConstructor
@AllArgsConstructor
public class CheckstyleSourceFile extends BaseEntity {

  @Column(name = "name")
  private String name;

  @OneToMany(mappedBy = "file")
  private List<CheckstyleError> errors;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "checkstyle_report_id")
  private CheckstyleReportEntity report;

  public CheckstyleSourceFile(String name, CheckstyleReportEntity report) {
    this.name = name;
    this.report = report;
  }
}
