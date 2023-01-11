package com.example.solutionservice.entities.checkstyle;

import com.example.solutionservice.entities.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

  @ManyToOne
  @JoinColumn(name = "checkstyle_report_id")
  private CheckstyleReport report;

}
