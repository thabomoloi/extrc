package com.extrc.view.web;

import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.services.DefeasibleReasoner;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.reasoning.reasoners.RationalReasoner;
import com.extrc.view.Validator;
import com.extrc.view.web.helpers.ParserValidation;

import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;

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

    app.get("/api/validate/query/{formula}", ctx -> {
      String formula = ctx.pathParam("formula");
      Validator.Node validation = validator.validateFormula(formula);
      if (validation.isValid) {
        ctx.sessionAttribute("formula", formula);
        ctx.json(new ParserValidation(true, "The query formula is valid."));
      } else {
        ctx.json(new ParserValidation(true, "The query formula \"" + formula + "\" is invalid."));
      }
    });

    app.get("/api/validate/formulas/{formulas}", ctx -> {
      String formulas = ctx.pathParam("formulas");
      Validator.Node validation = validator.validateFormulas(formulas);
      if (validation.isValid) {
        ctx.sessionAttribute("knowledgeBase", formulas);
        ctx.json(new ParserValidation(true, "The knowledge base is valid."));
      } else {
        ctx.json(new ParserValidation(true, "The knowledge base contains at least one invalid formula."));
      }
    });

    app.get("/api/entailment/rational", ctx -> {
      String formula = ctx.sessionAttribute("formula");
      String formulas = ctx.sessionAttribute("formulas");
      if (formula == null || formulas == null) {
        throw new BadRequestResponse("Either the query formula or knowledge base is invalid.");
      } else {
        Validator.Node queryValidation = validator.validateFormulas(formula);
        Validator.Node kbValidation = validator.validateFormulas(formulas);
        if (queryValidation.isValid && kbValidation.isValid) {
          DefeasibleReasoner reasoner = new RationalReasoner((KnowledgeBase) kbValidation.parsedObject);
          ctx.json(reasoner.query((PlFormula) queryValidation.parsedObject));
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
        Validator.Node queryValidation = validator.validateFormulas(formula);
        Validator.Node kbValidation = validator.validateFormulas(formulas);
        if (queryValidation.isValid && kbValidation.isValid) {
          DefeasibleReasoner reasoner = new RationalReasoner((KnowledgeBase) kbValidation.parsedObject);
          ctx.json(reasoner.query((PlFormula) queryValidation.parsedObject));
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
