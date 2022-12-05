package org.example.project;

import java.io.File;
import java.io.InputStream;
import lombok.AllArgsConstructor;
import org.zeroturnaround.zip.ZipUtil;

@AllArgsConstructor
public class ProjectLoader {

  private String filePath;
  private ProjectDao dao;

  public File loadProject(Long solutionId) {
    File sourceFolder = new File(filePath);
    InputStream zipStream = dao.getZipStream(solutionId);
    ZipUtil.unpack(zipStream, sourceFolder);
    return sourceFolder;
  }

}
