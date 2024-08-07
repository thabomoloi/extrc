package com.extrc.services;

import com.extrc.models.BaseRank;
import com.extrc.models.Entailment;

public interface ReasonerService {
  public Entailment getEntailment(BaseRank baseRank);
}
