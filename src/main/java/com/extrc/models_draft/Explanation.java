package com.extrc.models_draft;

import java.util.ArrayList;

public class Explanation implements IExplanation {
  protected String name;
  protected ArrayList<IObserver> observers = new ArrayList<IObserver>();

  public Explanation(String name) {
    this.name = name;
  }

  public void show(String message) {
    System.out.println(message);
    // notifySubscribers(message);
  }

  @Override
  public void addSubscriber(IObserver observer) {
    observers.add(observer);
  }

  @Override
  public void removeSubscriber(IObserver observer) {
    observers.remove(observer);
  }

  @Override
  public void notifySubscribers(String explanation) {
    observers.forEach(observer -> observer.notification(name, explanation));
  }
}
