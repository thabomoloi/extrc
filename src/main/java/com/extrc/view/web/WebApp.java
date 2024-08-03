package com.extrc.view.web;

import java.util.List;

import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.services.DefeasibleReasoner;
import com.extrc.common.structures.DefeasibleImplication;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.reasoning.reasoners.LexicalReasoner;
import com.extrc.reasoning.reasoners.RationalReasoner;
import com.extrc.view.Validator;
import com.extrc.view.web.helpers.EntailmentJson;
import com.extrc.view.web.helpers.ParserValidation;

import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.UploadedFile;

public class WebApp {

  public static void run() {
    Validator validator = new Validator();

    var app = Javalin.create(
        config -> {
          config.staticFiles.add("/web");
          // config.staticFiles.add("/"); // Other static assets, external to the ReactJS
          config.spaRoot.addFile("/", "/web/index.html"); // Catch-all route for the single-page application
        });

    app.start(8080);

    app.get("/api/query", ctx -> {
      ctx.json(new Object() {
        @SuppressWarnings("unused")
        public final String formula = ctx.sessionAttribute("formula");
      });
    });

    app.get("/api/formulas", ctx -> {
      ctx.json(new Object() {
        @SuppressWarnings("unused")
        public final String formulas = ctx.sessionAttribute("formulas");
      });
    });

    app.get("/api/validate/query/{formula}", ctx -> {
      String formula = ctx.pathParam("formula");
      Validator.Node validation = validator.validateFormula(formula);

      boolean isDefeasibleImplication = false;
      try {
        isDefeasibleImplication = validation.parsedObject instanceof DefeasibleImplication;
      } catch (ClassCastException e) {
      }
      String message;
      if (validation.isValid && isDefeasibleImplication) {
        ctx.sessionAttribute("formula", validation.parsedObject.toString());
        message = "The query formula is valid.";
      } else {
        message = "The query formula \"" + formula + "\" is invalid.";
      }
      ctx.json(new ParserValidation(validation.isValid && isDefeasibleImplication, message));
    });

    app.get("/api/validate/formulas/{formulas}", ctx -> {
      String formulas = ctx.pathParam("formulas");
      Validator.Node validation = validator.validateFormulas(formulas);
      String message;
      if (validation.isValid) {
        ctx.sessionAttribute("formulas", validation.parsedObject.toString().replaceAll("[{}]", ""));
        message = "The knowledge base is valid.";
      } else {
        message = "The knowledge base contains at least one invalid formula.";
      }
      ctx.json(new ParserValidation(validation.isValid, message));
    });

    app.post("/api/validate/file", ctx -> {
      List<UploadedFile> files = ctx.uploadedFiles("file");
      if (!files.isEmpty()) {
        UploadedFile file = files.get(0);
        Validator.Node validation = validator.validateInputStream(file.content());
        String message;
        if (validation.isValid) {
          ctx.sessionAttribute("formulas", validation.parsedObject.toString().replaceAll("[{}]", ""));
          message = "The knowledge base is valid.";
        } else {
          message = "The knowledge base contains at least one invalid formula.";
        }
        ctx.json(new ParserValidation(validation.isValid, message));
      } else {
        ctx.status(400).result("No file uploaded");
      }
    });

    app.get("/api/entailment/rational", ctx -> {
      String formula = ctx.sessionAttribute("formula");
      String formulas = ctx.sessionAttribute("formulas");
      if (formula == null || formulas == null) {
        throw new BadRequestResponse("Either the query formula or knowledge base is invalid.");
      } else {
        Validator.Node queryValidation = validator.validateFormula(formula);
        Validator.Node kbValidation = validator.validateFormulas(formulas);
        if (queryValidation.isValid && kbValidation.isValid) {
          DefeasibleReasoner reasoner = new RationalReasoner((KnowledgeBase) kbValidation.parsedObject);
          ctx.json(new EntailmentJson(reasoner.query((PlFormula) queryValidation.parsedObject)));
        } else {
          throw new BadRequestResponse("Either the query formula or knowledge base is invalid.");
        }
      }
    });

    app.get("/api/entailment/lexical", ctx -> {
      String formula = ctx.sessionAttribute("formula");
      String formulas = ctx.sessionAttribute("formulas");
      if (formula == null || formulas == null) {
        throw new BadRequestResponse("Either the query formula or knowledge base is invalid.");
      } else {
        Validator.Node queryValidation = validator.validateFormula(formula);
        Validator.Node kbValidation = validator.validateFormulas(formulas);
        if (queryValidation.isValid && kbValidation.isValid) {
          DefeasibleReasoner reasoner = new LexicalReasoner((KnowledgeBase) kbValidation.parsedObject);
          ctx.json(new EntailmentJson(reasoner.query((PlFormula) queryValidation.parsedObject)));
        } else {
          throw new BadRequestResponse("Either the query formula or knowledge base is invalid.");
        }
      }
    });

    app.get("/api/invalidate-session", ctx -> {
      ctx.req().getSession().invalidate();
      ctx.result("Session invalidated");
    });

    app.get("/api/*", ctx -> ctx.status(400)); // Any unmapped API will result in a 400 Bad Request
  }
}
