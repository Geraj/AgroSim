package control.observer;

import java.util.LinkedList;
/**
 * 
 * Interface for observe pattern for state machine
 * 
 * @author Geraj
 */
public interface Observable {
  public void registerObserver(Listener listener);

  public void unRegisterObserver(Listener listener);

  void notifyObservers();
}