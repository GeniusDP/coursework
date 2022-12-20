package com.zaranik.coursework.checkerservice.services;

import com.zaranik.coursework.checkerservice.dtos.container.response.checkstyle.CheckstyleReport;
import com.zaranik.coursework.checkerservice.dtos.container.response.checkstyle.SourceFile;
import com.zaranik.coursework.checkerservice.dtos.container.response.checkstyle.StyleError;
import com.zaranik.coursework.checkerservice.entities.checkstyle.CheckstyleError;
import com.zaranik.coursework.checkerservice.entities.checkstyle.CheckstyleReportEntity;
import com.zaranik.coursework.checkerservice.entities.checkstyle.CheckstyleSourceFile;
import com.zaranik.coursework.checkerservice.repositories.CheckstyleErrorRepository;
import com.zaranik.coursework.checkerservice.repositories.CheckstyleReportRepository;
import com.zaranik.coursework.checkerservice.repositories.CheckstyleSourceFileRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckstyleReportService {

  private final CheckstyleReportRepository checkstyleReportRepository;
  private final CheckstyleErrorRepository checkstyleErrorRepository;
  private final CheckstyleSourceFileRepository checkstyleSourceFileRepository;

  @Transactional
  public CheckstyleReportEntity saveCheckstyleReport(CheckstyleReport report) {
    if(report == null){
      return null;
    }

    CheckstyleReportEntity entity = new CheckstyleReportEntity();
    checkstyleReportRepository.save(entity);
    List<CheckstyleSourceFile> files = new ArrayList<>();
    for (SourceFile sourceFile : report.getSourceFiles()) {
      CheckstyleSourceFile checkstyleSourceFile = new CheckstyleSourceFile(sourceFile.getFileName(), entity);
      files.add(checkstyleSourceFileRepository.save(checkstyleSourceFile));
      List<CheckstyleError> errors = checkstyleErrorRepository.saveAll(copyErrors(sourceFile, checkstyleSourceFile));
      checkstyleSourceFile.setErrors(errors);
    }
    entity.setCheckstyleSourceFile(files);
    return checkstyleReportRepository.save(entity);
  }

  private Iterable<CheckstyleError> copyErrors(SourceFile file, CheckstyleSourceFile checkstyleSourceFile) {
    List<CheckstyleError> errors = new ArrayList<>();
    for (StyleError error : file.getErrors()) {
      CheckstyleError checkstyleError = CheckstyleError.builder()
        .message(error.getMessage())
        .line(error.getLine())
        .source(error.getSource())
        .file(checkstyleSourceFile)
        .build();
      errors.add(checkstyleError);
    }
    return errors;
  }

}
