package org.example.project.checkers;


import java.io.File;
import lombok.AllArgsConstructor;
import org.example.project.ProjectDao;

@AllArgsConstructor
public abstract class Checker implements Runnable {
  protected Long solutionId;
  protected File mainDir;
  protected ProjectDao dao;
}
