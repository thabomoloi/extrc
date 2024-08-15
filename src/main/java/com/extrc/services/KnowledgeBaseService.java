package com.extrc.services;

import io.javalin.http.Context;

import com.extrc.models.KnowledgeBase;

public interface KnowledgeBaseService {

  public KnowledgeBase getKnowledgeBase(Context ctx);

  public void saveKnowledgeBase(Context ctx, KnowledgeBase knowledgeBase);
}
