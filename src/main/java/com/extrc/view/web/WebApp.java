package com.extrc.view.web;

import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.view.Validator;

import io.javalin.Javalin;

public class WebApp {
  static class Error {
    private final String error;

    public Error(String error) {
      this.error = error;
    }

    public String getError() {
      return error;
    }
  }

  public static void run() {
    Validator validator = new Validator();
    // var app = Javalin.create(/* config */)
    // .get("/", ctx -> ctx.result("Hello World"))
    // .start(7070);

    // app.get("/entail/{algorithm}/{formula}", ctx -> {
    // String algorithm = ctx.pathParam("algorithm");
    // String formula = ctx.pathParam("formula");
    // Validator.Node validated = validator.validateFormula(formula);
    // if (validated.isValid) {
    // PlFormula query = (PlFormula) validated.parsedObject;

    // ctx.result("Received parameter: " + formula);
    // } else {
    // ctx.json(new Error("Invalid formula: " + formula)).status(400);
    // }
    // });
    // Javalin app = Javalin.create()
    // .get("/api/users/:username", UserController.user)
    // .get("/api/*", ctx -> ctx.status(400)); // Any unmapped API will result in a
    // 400 Bad Request
    // .get("version", VersionController.version);

    // app.config
    // .addStaticFiles("/web") // The ReactJS application
    // .addStaticFiles("/") // Other static assets, external to the ReactJS
    // application
    // .addSinglePageRoot("/", "/web/index.html"); // Catch-all route for the
    // single-page application
    var app = Javalin.create(
        config -> {
          config.staticFiles.add("/web");
          // config.staticFiles.add("/");
          config.spaRoot.addFile("/", "/web/index.html"); // Catch-all route for the single-page application
        });
    app.start(8080);
  }
}
