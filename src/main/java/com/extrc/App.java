package com.extrc;

import java.io.File;

import com.extrc.apps.Application;
import com.extrc.apps.ApplicationFactory;

public class App {
  public static void main(String[] args) {
    try {
      Application app = ApplicationFactory.createApplication(args[0]);
      app.run();
    } catch (ArrayIndexOutOfBoundsException e) {
      String appName = (new File(App.class.getProtectionDomain()
          .getCodeSource()
          .getLocation()
          .getPath()))
          .getName();
      System.out.println("Usage: java -jar " + appName + " [console/web]");
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }
}
