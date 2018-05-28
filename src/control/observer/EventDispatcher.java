package control.observer;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * Event dispatcer for state machine
 * 
 * @author Geraj
 */
public class EventDispatcher implements EventProducer, StateListener {

  private static EventDispatcher instance = null;

  public static EventDispatcher getInstance() {
    if (instance == null) {
      instance = new EventDispatcher();
    }
    return instance;
  }

  public ConcurrentHashMap<StateListener, LinkedList<StateMachineEvents>> registeredListeners = new ConcurrentHashMap<StateListener, LinkedList<StateMachineEvents>>();

  /**
   * Fire events to all listeners subscribed
   */
  @Override
  public void fireEvent(StateMachineEvents event, Object parameter, Object source) {
    for (StateListener listeners : this.registeredListeners.keySet()) {
      if (listeners != null) {
        if (this.registeredListeners.get(listeners) != null && this.registeredListeners.get(
            listeners).contains(event)) {
          listeners.handleEvent(event, parameter, source);
        }
      }
    }

  }

  /**
   * React and relay all events to listeners which are interested
   */
  @Override
  public void handleEvent(StateMachineEvents eventType, Object message, Object source) {
    this.fireEvent(eventType, message, source);
  }

  public void registerListener(StateListener listener, StateMachineEvents event) {
    if (this.registeredListeners.containsKey(listener)) {
      this.registeredListeners.get(listener).add(event);
    } else {
      LinkedList<StateMachineEvents> list = new LinkedList<StateMachineEvents>();
      list.add(event);
      this.registeredListeners.put(listener, list);
    }
  }

  public void unRegisterListener(StateListener listener, StateMachineEvents event) {
    if (this.registeredListeners.keySet().contains(listener)) {
      this.registeredListeners.get(listener).remove(event);
    }
  }

  public void unRegisterListener(StateListener listener) {
    this.registeredListeners.remove(listener);
  }
}
