package com.extrc.controllers;

import com.extrc.models.BaseRank;
import com.extrc.services.ReasonerFactory;
import com.extrc.services.ReasonerService;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.UnprocessableContentResponse;

public class ReasonerController {

  public static void getEntailment(Context ctx) {
    String reasonerType = ctx.pathParam("reasoner");

    try {
      BaseRank baseRank = ctx.bodyValidator(BaseRank.class)
          .check(v -> v.getQueryInput() != null, "Query input is required")
          .check(v -> v.getRanking() != null, "Ranking is required").get();
      ReasonerService reasoner = ReasonerFactory.createReasoner(reasonerType);
      ctx.json(reasoner.getEntailment(baseRank));
    } catch (IllegalArgumentException e) {
      throw new BadRequestResponse(e.getMessage());
    } catch (Exception e) {
      // Handle deserialization error
      throw new UnprocessableContentResponse("Failed to deserialize QueryInput: " + e.getMessage());
    }
  }

}
