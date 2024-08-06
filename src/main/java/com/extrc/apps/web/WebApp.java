package com.extrc.apps.web;

import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.apps.Application;
import com.extrc.config.ObjectMapperConfig;
import com.extrc.models.DefeasibleImplication;
import com.extrc.models.KnowledgeBase;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class WebApp implements Application {
  @Override
  public void run() {
    Javalin app = Javalin.create(config -> {
      config.jsonMapper(new JavalinJackson(ObjectMapperConfig.createObjectMapper(), true));
    });
    app.start(8080);

    // Routes
    app.get("/api", ctx -> {
      Proposition a = new Proposition("a");
      Proposition b = new Proposition("b");
      Proposition c = new Proposition("c");
      KnowledgeBase kb = new KnowledgeBase();
      kb.add(new DefeasibleImplication(a, b));
      kb.add(new DefeasibleImplication(b, c));
      ctx.json(kb);
    });
  }
}
