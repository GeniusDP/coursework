package com.example.solutionservice.entities.checkstyle;

import com.example.solutionservice.entities.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@Table(name = "checkstyle_reports")
@AllArgsConstructor
public class CheckstyleReport extends BaseEntity {

}
