package com.extrc.models;

import java.util.ArrayList;

public class Explanation {
  public enum Title {
    BaseRank,
    RationalClosure,
  }

  private final Title title;
  private final ArrayList<String> steps;

  public Explanation(Title title) {
    this.title = title;
    this.steps = new ArrayList<>();
  }

  public void addStep(String step) {
    steps.add(step);
  }

  public String getTitle() {
    return title.toString();
  }

  public ArrayList<String> getSteps() {
    return steps;
  }
}
