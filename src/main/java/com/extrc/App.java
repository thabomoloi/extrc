package com.extrc;

// import java.io.File;

// import com.extrc.apps.Application;
// import com.extrc.apps.ApplicationFactory;
import com.extrc.config.ObjectMapperConfig;
import com.extrc.controllers.BaseRankController;
import com.extrc.controllers.QueryInputController;
import com.extrc.controllers.ReasonerController;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class App {
  public static void main(String[] args) {
    // try {
    //   Application app = ApplicationFactory.createApplication(args[0]);
    //   app.run();
    // } catch (ArrayIndexOutOfBoundsException e) {
    //   String appName = (new File(App.class.getProtectionDomain()
    //       .getCodeSource()
    //       .getLocation()
    //       .getPath()))
    //       .getName();
    //   System.out.println("Usage: java -jar " + appName + " [console/web]");
    // } catch (IllegalArgumentException e) {
    //   System.out.println(e.getMessage());
    // }
    Javalin app = Javalin.create(config -> {
      config.jsonMapper(new JavalinJackson(ObjectMapperConfig.createObjectMapper(), true));
      config.staticFiles.add("/web");
      // config.staticFiles.add("/"); // Other static assets, external to the ReactJS
      config.spaRoot.addFile("/", "/web/index.html"); // Catch-all route for the single-page application
    });
    app.start(8080);

    // Routes
    app.get("/api/query", QueryInputController::getQueryInput);
    app.post("/api/query", QueryInputController::createQueryInput);
    app.post("/api/query/file", QueryInputController::createKbFromFile);
    app.post("/api/base-rank", BaseRankController::getBaseRank);
    app.post("/api/entailment/{reasoner}", ReasonerController::getEntailment);
  }
}
