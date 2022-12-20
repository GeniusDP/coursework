package com.zaranik.coursework.checkerservice.entities.pmd;

import com.zaranik.coursework.checkerservice.entities.BaseEntity;
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
@Table(name = "pmd_files")
@NoArgsConstructor
@AllArgsConstructor
public class PmdReportSourceFile extends BaseEntity {

  @Column(name = "name")
  private String name;

  @ManyToOne
  @JoinColumn(name = "pmd_report_id")
  private PmdReport pmdReport;

}
