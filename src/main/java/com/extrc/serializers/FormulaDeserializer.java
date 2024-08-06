package com.extrc.serializers;

import java.io.IOException;

import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.utils.DefeasibleParser;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class FormulaDeserializer extends JsonDeserializer<PlFormula> {

  @Override
  public PlFormula deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
    String formulaString = p.getValueAsString();
    try {
      DefeasibleParser parser = new DefeasibleParser();
      return parser.parseFormula(formulaString);
    } catch (IOException | ParserException e) {
      throw new IOException("Failed to deserialize PlFormula", e);
    }
  }

}
