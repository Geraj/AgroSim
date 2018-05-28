package control.observer;

import java.util.LinkedList;
/**
 * 
 * Interface for observe pattern for state machine
 * 
 * @author Geraj
 */
public interface Observable {
  public void registerObserver(StateListener listener);

  public void unRegisterObserver(StateListener listener);

  void notifyObservers();
}