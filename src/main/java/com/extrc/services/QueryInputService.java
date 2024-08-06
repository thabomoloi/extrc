package com.extrc.services;

import io.javalin.http.Context;

import com.extrc.models.QueryInput;

public interface QueryInputService {

  public QueryInput getQueryInput(Context ctx);

  public void saveQueryInput(Context ctx, QueryInput queryInput);
}
