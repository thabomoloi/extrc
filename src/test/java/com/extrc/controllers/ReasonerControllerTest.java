package com.extrc.controllers;

import com.extrc.models.BaseRank;
import com.extrc.models.ErrorResponse;
import com.extrc.models.KnowledgeBase;
import com.extrc.utils.DefeasibleParser;

import io.javalin.http.Context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.io.InputStream;

import com.extrc.models.Entailment;
import com.extrc.services.BaseRankService;
import com.extrc.services.BaseRankServiceImpl;

public class ReasonerControllerTest {

  private Context ctx;
  private DefeasibleParser parser;
  private BaseRankService baseRankService;

  @BeforeEach
  public void setUp() {
    ctx = mock(Context.class);
    parser = new DefeasibleParser();
    baseRankService = new BaseRankServiceImpl();
  }

  @Test
  public void testGetEntailmentRationalSuccess1() throws Exception {
    String reasonerType = "rational";
    String formula = "p ~> !f";

    when(ctx.pathParam("reasoner")).thenReturn(reasonerType);
    when(ctx.pathParam("queryFormula")).thenReturn(formula);

    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("kb1.txt");
    KnowledgeBase kb = parser.parseInputStream(inputStream);
    BaseRank baseRank = baseRankService.constructBaseRank(kb);

    when(ctx.bodyAsClass(BaseRank.class)).thenReturn(baseRank);

    ReasonerController.getEntailment(ctx);
    verify(ctx).status(200);
    verify(ctx).json(argThat(response -> {
      Entailment entailment = (Entailment) response;
      return entailment.getTimeTaken() > 0 && entailment.getEntailed();
    }));
  }

  @Test
  public void testGetEntailmentRationalSuccess2() throws Exception {
    String reasonerType = "rational";
    String formula = "p ~> w";

    when(ctx.pathParam("reasoner")).thenReturn(reasonerType);
    when(ctx.pathParam("queryFormula")).thenReturn(formula);

    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("kb1.txt");
    KnowledgeBase kb = parser.parseInputStream(inputStream);
    BaseRank baseRank = baseRankService.constructBaseRank(kb);

    when(ctx.bodyAsClass(BaseRank.class)).thenReturn(baseRank);

    ReasonerController.getEntailment(ctx);
    verify(ctx).status(200);
    verify(ctx).json(argThat(response -> {
      Entailment entailment = (Entailment) response;
      return entailment.getTimeTaken() > 0 && !entailment.getEntailed();
    }));
  }

  @Test
  public void testGetEntailmentLexicalSuccess1() throws Exception {
    String reasonerType = "lexical";
    String formula = "p ~> !f";

    when(ctx.pathParam("reasoner")).thenReturn(reasonerType);
    when(ctx.pathParam("queryFormula")).thenReturn(formula);

    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("kb1.txt");
    KnowledgeBase kb = parser.parseInputStream(inputStream);
    BaseRank baseRank = baseRankService.constructBaseRank(kb);

    when(ctx.bodyAsClass(BaseRank.class)).thenReturn(baseRank);

    ReasonerController.getEntailment(ctx);
    verify(ctx).status(200);
    verify(ctx).json(argThat(response -> {
      Entailment entailment = (Entailment) response;
      return entailment.getTimeTaken() > 0 && entailment.getEntailed();
    }));
  }

  @Test
  public void testGetEntailmentLexicalSuccess2() throws Exception {
    String reasonerType = "lexical";
    String formula = "p ~> w";

    when(ctx.pathParam("reasoner")).thenReturn(reasonerType);
    when(ctx.pathParam("queryFormula")).thenReturn(formula);

    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("kb1.txt");
    KnowledgeBase kb = parser.parseInputStream(inputStream);
    BaseRank baseRank = baseRankService.constructBaseRank(kb);

    when(ctx.bodyAsClass(BaseRank.class)).thenReturn(baseRank);

    ReasonerController.getEntailment(ctx);
    verify(ctx).status(200);
    verify(ctx).json(argThat(response -> {
      Entailment entailment = (Entailment) response;
      return entailment.getTimeTaken() > 0 && entailment.getEntailed();
    }));
  }

  @Test
  public void testGetEntailmentInvalidFormula() throws Exception {
    String reasonerType = "lexical";
    String formula = "p > w";

    when(ctx.pathParam("reasoner")).thenReturn(reasonerType);
    when(ctx.pathParam("queryFormula")).thenReturn(formula);

    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("kb1.txt");
    KnowledgeBase kb = parser.parseInputStream(inputStream);
    BaseRank baseRank = baseRankService.constructBaseRank(kb);

    when(ctx.bodyAsClass(BaseRank.class)).thenReturn(baseRank);

    ReasonerController.getEntailment(ctx);

    ArgumentCaptor<ErrorResponse> errorCaptor = ArgumentCaptor.forClass(ErrorResponse.class);
    verify(ctx).status(400);
    verify(ctx).json(errorCaptor.capture());

    ErrorResponse error = errorCaptor.getValue();
    assertTrue(error.message.equals("Invalid query formula: " + formula));
  }

  @Test
  public void testGetEntailmentInvalidReasoner() throws Exception {
    String reasonerType = "invalid";
    String formula = "p ~> w";

    when(ctx.pathParam("reasoner")).thenReturn(reasonerType);
    when(ctx.pathParam("queryFormula")).thenReturn(formula);

    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("kb1.txt");
    KnowledgeBase kb = parser.parseInputStream(inputStream);
    BaseRank baseRank = baseRankService.constructBaseRank(kb);

    when(ctx.bodyAsClass(BaseRank.class)).thenReturn(baseRank);

    ReasonerController.getEntailment(ctx);

    ArgumentCaptor<ErrorResponse> errorCaptor = ArgumentCaptor.forClass(ErrorResponse.class);
    verify(ctx).status(400);
    verify(ctx).json(errorCaptor.capture());

    ErrorResponse error = errorCaptor.getValue();
    assertTrue(error.message.equals("Invalid reasoner: " + reasonerType));
  }
}
