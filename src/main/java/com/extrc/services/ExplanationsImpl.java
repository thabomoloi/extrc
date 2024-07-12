package com.extrc.services;

import com.extrc.models.Explanation;

public class ExplanationsImpl implements IExplanations {
  private final Explanation[] explanations;

  public ExplanationsImpl() {
    explanations = new Explanation[Explanation.Title.values().length];
  }

  @Override
  public void clear() {
    // Clear all elements in explanations array
    for (int i = 0; i < explanations.length; i++) {
      explanations[i] = null;
    }
  }

  @Override
  public void addExplanationStep(Explanation.Title title, String step) {
    int idx = title.ordinal();
    Explanation explanation = explanations[idx];
    if (explanation == null) {
      explanation = new Explanation(title);
      explanations[idx] = explanation;
    }
    explanation.addStep(step);
  }

  @Override
  public Explanation findExplanation(Explanation.Title title) {
    int idx = title.ordinal();
    return explanations[idx];
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    for (Explanation explanation : explanations) {
      if (explanation != null) {
        builder.append(explanation.getTitle()).append(":\n===========================\n");
        for (String step : explanation.getSteps()) {
          builder.append(step).append("\n");
        }
        builder.append("\n");
      }
    }

    return builder.toString().trim();
  }
}
