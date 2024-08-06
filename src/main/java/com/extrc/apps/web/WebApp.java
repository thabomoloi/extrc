package com.extrc.apps.web;

import com.extrc.apps.Application;
import com.extrc.config.ObjectMapperConfig;
import com.extrc.controllers.QueryInputController;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class WebApp implements Application {

  @Override
  public void run() {
    Javalin app = Javalin.create(config -> {
      config.jsonMapper(new JavalinJackson(ObjectMapperConfig.createObjectMapper(), true));
    });
    app.start(8080);

    // Routes
    app.get("/api/query", QueryInputController::getQueryInput);
    app.post("/api/query", QueryInputController::createQueryInput);
  }
}
