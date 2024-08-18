package com.extrc.controllers;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.extrc.models.BaseRank;
import com.extrc.models.ErrorResponse;
import com.extrc.models.KnowledgeBase;
import com.extrc.utils.DefeasibleParser;

import io.javalin.http.Context;

public class BaseRankControllerTest {
  private Context ctx;
  private DefeasibleParser parser;

  @BeforeEach
  public void setUp() {
    ctx = mock(Context.class);
    parser = new DefeasibleParser();
  }

  @Test
  public void testGetBaseRankValid() throws Exception {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("kb1.txt");
    KnowledgeBase kb = parser.parseInputStream(inputStream);
    when(ctx.bodyAsClass(KnowledgeBase.class)).thenReturn(kb);
    BaseRankController.getBaseRank(ctx);
    verify(ctx).status(200);
    verify(ctx).json(argThat(response -> {
      BaseRank baseRank = (BaseRank) response;
      return baseRank.getRanking().size() == 3;
    }));
  }

  @Test
  public void testGetBaseRankInValid() {
    when(ctx.bodyAsClass(KnowledgeBase.class)).thenThrow(new RuntimeException("Invalid"));
    BaseRankController.getBaseRank(ctx);
    ArgumentCaptor<ErrorResponse> errorCaptor = ArgumentCaptor.forClass(ErrorResponse.class);
    verify(ctx).status(400);
    verify(ctx).json(errorCaptor.capture());

    ErrorResponse error = errorCaptor.getValue();
    assertTrue(error.message.equals("The knowledge base is invalid"));
  }

}
