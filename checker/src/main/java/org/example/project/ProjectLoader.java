package org.example.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.example.project.dtos.SolutionDto;
import org.example.project.exceptions.CouldNotFetchSolutionException;
import org.zeroturnaround.zip.ZipUtil;

public class ProjectLoader {

  public static File fetchProject(Long solutionId) throws Exception {
    File sourceFolder = new File("solution");
    File taskZip = fetchTaskInZip(solutionId);
    FileInputStream fis = new FileInputStream(taskZip);
    ZipUtil.unpack(fis, sourceFolder);
    return sourceFolder;
  }

  private static File fetchTaskInZip(Long solutionId) throws IOException, UnirestException {
    String solutionPipeUrl = "http://solution-pipe-service:8085/api/solution-pipe-service/solutions/";
    ObjectMapper objectMapper = new ObjectMapper();

    Unirest.setTimeouts(0, 0);
    HttpResponse<String> response = Unirest
        .get(solutionPipeUrl + solutionId)
        .asString();

    int status = response.getStatus();
    if (status != 200) {
      throw new CouldNotFetchSolutionException();
    }
    String json = response.getBody();
    SolutionDto solutionDto = objectMapper.readValue(json, SolutionDto.class);

    byte[] bytes = solutionDto.bytes();
    File file = new File("task.zip");
    FileOutputStream fos = new FileOutputStream(file);
    fos.write(bytes);
    fos.close();
    return file;
  }

}
