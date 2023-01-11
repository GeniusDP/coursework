package com.zaranik.coursework.checkerservice.services;

import com.zaranik.coursework.checkerservice.dtos.container.response.pmd.PmdReport;
import com.zaranik.coursework.checkerservice.dtos.container.response.pmd.SourceFile;
import com.zaranik.coursework.checkerservice.dtos.container.response.pmd.Violation;
import com.zaranik.coursework.checkerservice.entities.pmd.PmdReportEntity;
import com.zaranik.coursework.checkerservice.entities.pmd.PmdReportSourceFile;
import com.zaranik.coursework.checkerservice.entities.pmd.PmdViolation;
import com.zaranik.coursework.checkerservice.repositories.PmdReportRepository;
import com.zaranik.coursework.checkerservice.repositories.PmdReportSourceFileRepository;
import com.zaranik.coursework.checkerservice.repositories.PmdViolationRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PmdReportService {

  private final PmdReportRepository pmdReportRepository;
  private final PmdViolationRepository pmdViolationRepository;
  private final PmdReportSourceFileRepository pmdReportSourceFileRepository;

  @Transactional
  public PmdReportEntity savePmdReport(PmdReport pmdReport) {
    if(pmdReport == null){
      return null;
    }

    PmdReportEntity entity = new PmdReportEntity();
    pmdReportRepository.save(entity);

    List<PmdReportSourceFile> files = new ArrayList<>();
    for (SourceFile sourceFile : pmdReport.getSourceFiles()) {
      PmdReportSourceFile pmdReportSourceFile = new PmdReportSourceFile(sourceFile.getFileName(), entity);
      files.add(pmdReportSourceFileRepository.save(pmdReportSourceFile));
      List<PmdViolation> violations = pmdViolationRepository.saveAll(copyViolations(sourceFile, pmdReportSourceFile));
      pmdReportSourceFile.setViolations(violations);
    }
    entity.setPmdReportSourceFiles(files);
    return pmdReportRepository.save(entity);
  }

  private List<PmdViolation> copyViolations(SourceFile sourceFile, PmdReportSourceFile pmdReportSourceFile) {
    List<PmdViolation> pmdViolations = new ArrayList<>();
    for (Violation violation : sourceFile.getViolations()) {
      PmdViolation pmdViolation = PmdViolation.builder()
        .value(violation.getValue())
        .beginLine(violation.getBeginLine())
        .endLine(violation.getEndLine())
        .className(violation.getClassName())
        .packageName(violation.getPackageName())
        .ruleName(violation.getRuleName())
        .packageName(violation.getMethodName())
        .file(pmdReportSourceFile)
        .build();
      pmdViolations.add(pmdViolation);
    }
    return pmdViolations;
  }

}
