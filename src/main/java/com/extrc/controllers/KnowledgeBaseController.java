package com.extrc.controllers;

import java.util.List;

import com.extrc.models.ErrorResponse;
import com.extrc.models.KnowledgeBase;
import com.extrc.services.KnowledgeBaseService;
import com.extrc.services.KnowledgeBaseServiceImpl;
import com.extrc.utils.DefeasibleParser;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

public class KnowledgeBaseController {
  private final static KnowledgeBaseService kbService = new KnowledgeBaseServiceImpl();

  public static void getKnowledgeBase(Context ctx) {
    ctx.status(200);
    ctx.json(kbService.getKnowledgeBase());
  }

  public static void createKb(Context ctx) {
    try {
      KnowledgeBase kb = ctx.bodyAsClass(KnowledgeBase.class);
      ctx.status(200);
      ctx.json(kb);
    } catch (Exception e) {
      ctx.status(400);
      ctx.json(new ErrorResponse(400, "Bad Request", "The knowledge base is invalid."));
    }
  }

  public static void createKbFromFile(Context ctx) {
    List<UploadedFile> files = ctx.uploadedFiles("file");
    try {
      if (!files.isEmpty()) {
        UploadedFile file = files.get(0);
        DefeasibleParser parser = new DefeasibleParser();
        KnowledgeBase kb = parser.parseInputStream(file.content());
        ctx.status(200);
        ctx.json(kb);
      } else {
        ctx.status(400);
        ctx.json(new ErrorResponse(400, "Bad Request", "No file uploaded"));
      }
    } catch (Exception e) {
      ctx.status(400);
      ctx.json(new ErrorResponse(400, "Bad Request", "The knowledge base is invalid."));
    }
  }
}
