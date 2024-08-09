package com.extrc.controllers;

import java.util.List;

import com.extrc.models.DefeasibleImplication;
import com.extrc.models.QueryInput;
import com.extrc.models.KnowledgeBase;
import com.extrc.services.QueryInputService;
import com.extrc.services.QueryInputServiceImpl;
import com.extrc.utils.DefeasibleParser;

import io.javalin.http.Context;
import io.javalin.http.UnprocessableContentResponse;
import io.javalin.http.UploadedFile;

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

  public static void createKbFromFile(Context ctx) {
    List<UploadedFile> files = ctx.uploadedFiles("file");
    try {
      if (!files.isEmpty()) {
        UploadedFile file = files.get(0);
        DefeasibleParser parser = new DefeasibleParser();
        KnowledgeBase kb = parser.parseInputStream(file.content());
        QueryInput queryInput = new QueryInput(queryInputService.getQueryInput(ctx).getQueryFormula(), kb);
        queryInputService.saveQueryInput(ctx, queryInput);
        ctx.json(queryInput);
      } else {
        ctx.status(400).result("No file uploaded");
      }
    } catch (Exception e) {
      throw new UnprocessableContentResponse("Failed to update knowledge base.");
    }
  }
}
