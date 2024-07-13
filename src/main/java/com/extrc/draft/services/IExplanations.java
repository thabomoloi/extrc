package com.extrc.draft.services;

import com.extrc.draft.models.Explanation;

public interface IExplanations {
  public void clear();

  public void addExplanationStep(Explanation.Title title, String step);

  public Explanation findExplanation(Explanation.Title title);
}
