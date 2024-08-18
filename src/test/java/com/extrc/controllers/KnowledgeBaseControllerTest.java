package com.extrc.controllers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.extrc.models.ErrorResponse;
import com.extrc.models.KnowledgeBase;
import com.extrc.utils.DefeasibleParser;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class KnowledgeBaseControllerTest {

  private Context ctx;
  private DefeasibleParser parser;

  @BeforeEach
  public void setUp() {
    ctx = mock(Context.class);
    parser = new DefeasibleParser();
  }

  @Test
  public void testGetKnowledgeBase() throws Exception {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("kb1.txt");
    KnowledgeBase kb = parser.parseInputStream(inputStream);

    KnowledgeBaseController.getKnowledgeBase(ctx);

    verify(ctx).status(200);
    verify(ctx).json(kb);
  }

  @Test
  public void testCreateKbValid() throws Exception {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("ranks10.txt");
    KnowledgeBase kb = parser.parseInputStream(inputStream);

    when(ctx.bodyAsClass(KnowledgeBase.class)).thenReturn(kb);

    KnowledgeBaseController.createKb(ctx);

    verify(ctx).status(200);
    verify(ctx).json(kb);
  }

  @Test
  public void testCreateKbInvalid() {
    when(ctx.bodyAsClass(KnowledgeBase.class)).thenThrow(new RuntimeException());

    KnowledgeBaseController.createKb(ctx);

    ArgumentCaptor<ErrorResponse> errorCaptor = ArgumentCaptor.forClass(ErrorResponse.class);
    verify(ctx).status(400);
    verify(ctx).json(errorCaptor.capture());

    ErrorResponse error = errorCaptor.getValue();
    assert (error.message.equals("The knowledge base is invalid."));
  }

  @Test
  public void testCreateKbFromFileValid() throws Exception {
    InputStream inputStream1 = getClass().getClassLoader().getResourceAsStream("kb1.txt");
    InputStream inputStream2 = getClass().getClassLoader().getResourceAsStream("kb1.txt");

    UploadedFile file = mock(UploadedFile.class);
    KnowledgeBase kb = parser.parseInputStream(inputStream1);

    when(file.content()).thenReturn(inputStream2);

    List<UploadedFile> files = new ArrayList<>();
    files.add(file);
    when(ctx.uploadedFiles("file")).thenReturn(files);

    KnowledgeBaseController.createKbFromFile(ctx);

    verify(ctx).json(kb);
  }

  @Test
  public void testCreateKbFromFileInvalid() throws IOException {
    UploadedFile file = mock(UploadedFile.class);
    when(file.content()).thenReturn(new ByteArrayInputStream("p > f".getBytes()));

    List<UploadedFile> files = new ArrayList<>();
    files.add(file);
    when(ctx.uploadedFiles("file")).thenReturn(files);

    KnowledgeBaseController.createKbFromFile(ctx);

    ArgumentCaptor<ErrorResponse> errorCaptor = ArgumentCaptor.forClass(ErrorResponse.class);
    verify(ctx).status(400);
    verify(ctx).json(errorCaptor.capture());

    ErrorResponse error = errorCaptor.getValue();
    assertTrue(error.message.equals("The knowledge base is invalid."));
  }

  @Test
  public void testCreateKbFromFileNoFileUploaded() {
    when(ctx.uploadedFiles("file")).thenReturn(new ArrayList<>());

    KnowledgeBaseController.createKbFromFile(ctx);

    ArgumentCaptor<ErrorResponse> errorCaptor = ArgumentCaptor.forClass(ErrorResponse.class);
    verify(ctx).status(400);
    verify(ctx).json(errorCaptor.capture());

    ErrorResponse error = errorCaptor.getValue();
    assertTrue(error.message.equals("No file uploaded"));
  }
}
