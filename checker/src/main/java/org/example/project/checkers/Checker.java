package org.example.project.checkers;


import java.io.File;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class Checker implements Runnable {

  protected File mainDir;

}
