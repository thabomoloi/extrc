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
    var app = Javalin.create(/* config */)
        .get("/", ctx -> ctx.result("Hello World"))
        .start(7070);

    app.get("/entail/{algorithm}/{formula}", ctx -> {
      String algorithm = ctx.pathParam("algorithm");
      String formula = ctx.pathParam("formula");
      Validator.Node validated = validator.validateFormula(formula);
      if (validated.isValid) {
        PlFormula query = (PlFormula) validated.parsedObject;

        ctx.result("Received parameter: " + formula);
      } else {
        ctx.json(new Error("Invalid formula: " + formula)).status(400);
      }
    });
  }
}
