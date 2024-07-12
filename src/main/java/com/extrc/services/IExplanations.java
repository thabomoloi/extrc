package com.extrc.services;

import com.extrc.models.Explanation;

public interface IExplanations {
  public void clear();

  public void addExplanationStep(Explanation.Title title, String step);

  public Explanation findExplanation(Explanation.Title title);
}
