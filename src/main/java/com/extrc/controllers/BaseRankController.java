package com.extrc.controllers;

import com.extrc.models.ErrorResponse;
import com.extrc.models.KnowledgeBase;
import com.extrc.services.BaseRankService;
import com.extrc.services.BaseRankServiceImpl;

import io.javalin.http.Context;

public class BaseRankController {
  private static final BaseRankService baseRankService = new BaseRankServiceImpl();

  public static void getBaseRank(Context ctx) {
    try {
      KnowledgeBase kb = ctx.bodyAsClass(KnowledgeBase.class);
      ctx.status(200);
      ctx.json(baseRankService.constructBaseRank(kb));
    } catch (Exception e) {
      ctx.status(400);
      ctx.json(new ErrorResponse(400, "Bad Request", "The knowledge base is invalid"));
    }
  }
}
