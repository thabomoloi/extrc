package com.extrc.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.models.DefeasibleImplication;
import com.extrc.models.KnowledgeBase;
import com.extrc.models.QueryInput;
import com.extrc.services.QueryInputService;
import com.extrc.utils.DefeasibleParser;

import io.javalin.http.Context;
import io.javalin.http.UnprocessableContentResponse;
import io.javalin.http.UploadedFile;
import io.javalin.validation.BodyValidator;

public class QueryInputControllerTest {

  private QueryInputService queryInputService;
  private Context ctx;

  @BeforeEach
  public void setUp() {
    queryInputService = mock(QueryInputService.class);
    ctx = mock(Context.class);
  }

  @Test
  public void testGetQueryInput() {
    // Setup
    Proposition p = new Proposition("p");
    Proposition b = new Proposition("b");
    Proposition f = new Proposition("f");
    Proposition w = new Proposition("w");

    KnowledgeBase kb = new KnowledgeBase();
    kb.add(new Implication(p, b));
    kb.add(new DefeasibleImplication(b, f));
    kb.add(new DefeasibleImplication(b, w));
    kb.add(new DefeasibleImplication(p, new Negation(f)));

    QueryInput queryInput = new QueryInput(new DefeasibleImplication(p, w), kb);
    when(queryInputService.getQueryInput(ctx)).thenReturn(queryInput);

    // Execute
    QueryInputController.getQueryInput(ctx);

    // Capture and verify
    ArgumentCaptor<QueryInput> queryInputCaptor = ArgumentCaptor.forClass(QueryInput.class);
    verify(ctx).json(queryInputCaptor.capture());
    QueryInput capturedQueryInput = queryInputCaptor.getValue();
    assertNotNull(capturedQueryInput);
    assertEquals(queryInput.getQueryFormula(), capturedQueryInput.getQueryFormula());
    assertEquals(queryInput.getKnowledgeBase(), capturedQueryInput.getKnowledgeBase());
  }

}
