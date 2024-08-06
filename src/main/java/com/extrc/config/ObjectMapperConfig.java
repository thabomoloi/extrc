package com.extrc.config;

import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.extrc.serializers.FormulaSerializer;

public class ObjectMapperConfig {
  public static ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(PlFormula.class, new FormulaSerializer());
    mapper.registerModule(module);
    return mapper;
  }
}