package org.example.project.checkers;


import java.io.File;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class AbstractChecker implements Checker {

  protected File taskDir;
}
