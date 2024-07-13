package com.extrc.reasoning;

import java.util.Collection;
import java.util.LinkedList;

public class Ranking extends LinkedList<Rank> {
  public Ranking() {
    super();
  }

  public Ranking(Collection<? extends Rank> ranks) {
    super(ranks);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (Rank rank : this) {
      builder.append(rank).append("\n");
    }
    return builder.toString().trim();
  }
}
