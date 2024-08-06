package com.extrc.controllers;

import com.extrc.models.QueryInput;

import io.javalin.http.Context;

public class QueryInputController {

  public static void getQueryInput(Context ctx) {

  }

  public static void createQueryInput(Context ctx) {
    ctx.bodyValidator(QueryInput.class);
    QueryInput queryInput = ctx.bodyAsClass(QueryInput.class);

    System.out.println(queryInput.getQueryFormula());
    ctx.json(queryInput);
  }
}
