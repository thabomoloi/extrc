package com.extrc.controllers;

import com.extrc.models.DefeasibleImplication;
import com.extrc.models.QueryInput;
import com.extrc.services.BaseRankService;
import com.extrc.services.BaseRankServiceImpl;

import io.javalin.http.Context;
import io.javalin.http.UnprocessableContentResponse;

public class BaseRankController {
  private static final BaseRankService baseRankService = new BaseRankServiceImpl();

  public static void getBaseRank(Context ctx) {
    try {
      QueryInput queryInput = ctx.bodyValidator(QueryInput.class)
          .check(v -> v.getQueryFormula() != null, "Query formula should not be null.")
          .check(v -> v.getQueryFormula() instanceof DefeasibleImplication,
              "Query formula should be defeasible implication.")
          .get();

      ctx.json(baseRankService.constructBaseRank(queryInput));
    } catch (Exception e) {
      // Handle deserialization error
      throw new UnprocessableContentResponse("Failed to deserialize QueryInput: " + e.getMessage());
    }
  }
}
