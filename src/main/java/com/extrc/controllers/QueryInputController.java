package com.extrc.controllers;

import com.extrc.models.DefeasibleImplication;
import com.extrc.models.QueryInput;
import com.extrc.services.QueryInputService;
import com.extrc.services.QueryInputServiceImpl;

import io.javalin.http.Context;
import io.javalin.http.UnprocessableContentResponse;

public class QueryInputController {
  private final static QueryInputService queryInputService = new QueryInputServiceImpl();

  public static void getQueryInput(Context ctx) {
    QueryInput queryInput = queryInputService.getQueryInput(ctx);
    ctx.json(queryInput);
  }

  public static void createQueryInput(Context ctx) {
    try {
      QueryInput queryInput = ctx.bodyValidator(QueryInput.class)
          .check(v -> v.getQueryFormula() != null, "Query formula should not be null.")
          .check(v -> v.getQueryFormula() instanceof DefeasibleImplication,
              "Query formula should be defeasible implication.")
          .get();
      queryInputService.saveQueryInput(ctx, queryInput);
      ctx.json(queryInput);
    } catch (Exception e) {
      // Handle deserialization error
      throw new UnprocessableContentResponse("Failed to deserialize QueryInput: " + e.getMessage());
    }
  }
}
