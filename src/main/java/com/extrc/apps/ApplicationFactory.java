package com.extrc.apps;

import com.extrc.apps.web.WebApp;
import com.extrc.apps.console.ConsoleApp;

public class ApplicationFactory {

  public static Application createApplication(String type) {
    return switch (type) {
      case "web" -> new WebApp();
      case "console" -> new ConsoleApp();
      default -> throw new IllegalArgumentException("Unknown application type: " + type);
    };
  }
}
