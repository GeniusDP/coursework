package com.zaranik.coursework.checkerservice.entities.pmd;

import com.zaranik.coursework.checkerservice.entities.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@Table(name = "pmd_reports")
@AllArgsConstructor
public class PmdReport extends BaseEntity {

}
