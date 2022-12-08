package com.zaranik.coursework.checkerservice.services;

import com.zaranik.coursework.checkerservice.dtos.CheckingReport;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.exceptions.ContainerRuntimeException;
import com.zaranik.coursework.checkerservice.repositories.CustomSolutionRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SolutionService {

  private final CustomSolutionRepository solutionRepository;

  @Value("${container.docker.start-command}")
  private String dockerStartCommand;

  @SneakyThrows
  public CheckingReport performChecking(MultipartFile solutionZip) {
    Solution solution = new Solution(solutionZip.getBytes());
    solutionRepository.save(solution);
    Long solutionId = solution.getId();

    File zipFile = createFileWithZip(solutionZip.getBytes());
    int statusCode = runContainer(zipFile.getName());

//    zipFile.delete();
    System.out.println(statusCode);

    if(statusCode != 0){
      throw new ContainerRuntimeException();
    }
    Solution result = solutionRepository.findById(solutionId);
    return new CheckingReport(
      result.getId(),
      result.getCompilationStatus(),
      result.getTestsNumber(),
      result.getTestsPassed(),
      result.getTestingStatus()
    );
  }

  private File createFileWithZip(byte[] data) {
    String zipFileName = UUID.randomUUID().toString();
    File file = new File(zipFileName);
    try (FileOutputStream outputStream = new FileOutputStream(file)) {
      outputStream.write(data);
    } catch (IOException e) {
      System.out.println(e);
    }
    return file;
  }

  private int runContainer(String zipFileName) throws IOException {
    String cmdTemplate = dockerStartCommand;
    String cmd = String.format(cmdTemplate, "/app/" + zipFileName);
    System.out.println(cmd);

    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd);
    Scanner scanner = new Scanner(process.getInputStream());
    StringBuilder sb = new StringBuilder();
    while (process.isAlive()) {
      if(scanner.hasNextLine()){
        String line = scanner.nextLine();
        System.out.println(line);
        sb.append(line);
      }
    }
    return process.exitValue();
  }

}
