package com.extrc.models_draft;

public interface IExplanation {
  public void addSubscriber(IObserver observer);

  public void removeSubscriber(IObserver observer);

  public void notifySubscribers(String explanation);
}
