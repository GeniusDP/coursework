package org.example.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import org.example.project.dtos.SolutionDto;
import org.example.project.dtos.TaskSourcesDto;
import org.example.project.exceptions.CouldNotFetchSolutionException;
import org.example.project.exceptions.NotValidArchiveStructureException;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;

public class ProjectLoader {

  private final static String host = System.getProperty("SOLUTION_PIPE_SERVICE_HOST", "localhost");
  private final static String url = "http://" + host + ":8085/api/solution-pipe-service";

  public static File fetchProject(Long taskId, Long solutionId) throws Exception {
    File sourceFolder = new File("solution");
    File taskZip = fetchTaskInZip(solutionId);
    FileInputStream fis = new FileInputStream(taskZip);
    ZipUtil.unpack(fis, sourceFolder);
    System.out.println("zip file of solution deleted: " + taskZip.delete());

    File taskDir = getTheOnlySubfolderInFolder(sourceFolder);
    try {
      deleteAllXmlConfigs(taskDir);
      String testFolderPath = taskDir.getAbsolutePath() + "/src/test";
      File testFolder = new File(testFolderPath);
      FileUtils.deleteDirectory(testFolder);
      //всё ненужное в решении ученика удалено
      //теперь вставляем нужное: тесты и официальные конфиги
      addFilesFromTaskSources(testFolderPath, taskDir.getPath(), taskId);
    } catch (Exception e) {
      throw new NotValidArchiveStructureException();
    }

    return taskDir;
  }

  private static void deleteAllXmlConfigs(File taskDir) {
    List<File> xmlConfigs = Arrays.stream(taskDir.listFiles())
      .filter(file -> file.getName().endsWith(".xml"))
      .toList();
    for (File config : xmlConfigs) {
      boolean delete = config.delete();
      if (!delete) {
        throw new IllegalArgumentException("Could not delete config " + config.getName() );
      }
    }
  }

  private static void addFilesFromTaskSources(String testFolderPath, String configsPath, Long taskId) throws IOException, UnirestException {
    TaskSourcesDto taskDto = getTaskSources(taskId);
    ZipUtil.unpack(new ByteArrayInputStream(taskDto.testSourceInZip()), new File(testFolderPath));

    File temporalDir = new File("temporal");
    ZipUtil.unpack(new ByteArrayInputStream(taskDto.sourceInZip()), temporalDir);

    File taskFolder = getTheOnlySubfolderInFolder(temporalDir);// /app/temporal/<task-folder-name>

    Arrays.stream(taskFolder.listFiles())
      .filter(file -> file.getName().endsWith(".xml"))
      .forEach(file -> {
        File newFile = new File(configsPath + "/" + file.getName());
        try {
          Files.move(file.toPath(), newFile.toPath());
        } catch (IOException e) {
          throw new NotValidArchiveStructureException();
        }
      });

    FileUtils.deleteDirectory(temporalDir);
  }

  private static TaskSourcesDto getTaskSources(Long taskId) throws IOException, UnirestException {
    String solutionPipeUrl = url + "/tasks/sources/";

    ObjectMapper objectMapper = new ObjectMapper();

    Unirest.setTimeouts(0, 0);
    HttpResponse<String> response = Unirest
      .get(solutionPipeUrl + taskId)
      .asString();

    int status = response.getStatus();
    if (status != 200) {
      throw new CouldNotFetchSolutionException();
    }
    String json = response.getBody();

    return objectMapper.readValue(json, TaskSourcesDto.class);
  }

  private static File fetchTaskInZip(Long solutionId) throws IOException, UnirestException {
    String solutionPipeUrl = url + "/submissions/sources/";
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

  private static File getTheOnlySubfolderInFolder(File folder) {
    if(!folder.isDirectory()){
      throw new NotValidArchiveStructureException(folder + " is not a folder");
    }
    List<File> list = Arrays.stream(folder.listFiles())
      .filter(File::isDirectory)
      .toList();
    if (list.size() != 1) {
      throw new NotValidArchiveStructureException(folder + " contains more than one subfolder");
    }
    return list.get(0);
  }

}
