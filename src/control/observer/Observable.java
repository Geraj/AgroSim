package control.observer;

import java.util.LinkedList;
/**
 * 
 * TODO DESCRIPTION
 * 
 * @author Geraj
 */
public interface Observable {
  public void registerObserver(Listener listener);

  public void unRegisterObserver(Listener listener);

  void notifyObservers();
}