package control.statemachine;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * Implementation of a state machine mechanism
 * 
 * @author Geraj
 * 
 */
public class StateMachine {

  private static Logger logger = Logger.getLogger(StateMachine.class);

  /** state map */
  private Map<String, State> stateMap = new HashMap<String, State>();

  /** the name of the catch all state */
  public static final String CATCH_ALL_STATE = "*";

  /** current state */
  private String currentState = null;

  /** the name of the state machine */
  private String name = null;

  /** loging enabled */
  private boolean detailedLogging = true;

  /**
   * Constructs a new StateMachine
   * 
   */
  public StateMachine() {
    super();

    // add the catch all state
    this.addState(CATCH_ALL_STATE);
  }

  /**
   * Gets the name
   * 
   * @return name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets name
   * 
   * @param name New value for name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the currentState name
   * 
   * @return currentState
   */
  public String getCurrentStateName() {
    return this.currentState;
  }

  /**
   * Add a state to the machine
   * 
   * @param stateName
   * @return the added state
   */
  public State addState(String stateName) {
    State ret = this.stateMap.get(stateName);
    if (ret == null) {
      // create new state
      ret = new State(stateName);
      ret.setStateMachine(this);
      this.stateMap.put(stateName, ret);
    }
    return ret;
  }

  /**
   * Obtain the state for the given name
   * 
   * @param stateName
   * @return the state object
   */
  public State getState(String stateName) {
    return this.stateMap.get(stateName);
  }

  /**
   * Change the state to the specified one. If handler specified than after exit and before entry
   * the hanler will be called with the specified parameter
   * 
   * @param newState if null is transition to current state
   * @param parameter
   * @param handler might be null
   */
  final void executeTransition(String newState, Object parameter, StateEventHandler handler) {
    //
    String destinationState = newState;
    // correct the new state
    if (destinationState == null) {
      destinationState = this.currentState;
    }

    if (destinationState != null && this.stateMap.containsKey(destinationState)
        && !destinationState.equals(CATCH_ALL_STATE)) {

      if (this.currentState != null) {
        this.stateMap.get(this.currentState).onExit(parameter);
      }

      // execute handling
      if (handler != null) {
        this.logDebug("execHandler p(" + parameter + ")");
        try {
          handler.handleEvent(parameter);
        } catch (Exception e) {
          this.logWarn("Exception caught when executing transition handler:", e);
        }
      }

      // set the current state
      this.currentState = destinationState;
      // call on entry
      this.stateMap.get(destinationState).onEntry(parameter);
    }
  }

  /**
   * Called to start the state machine
   * 
   * @param stateName
   */
  public void start(String stateName) {
    if (this.stateMap.containsKey(stateName) && !stateName.equals(CATCH_ALL_STATE)) {
      this.currentState = null;
      this.logDebug("start[" + stateName + "]");
      // change the state
      this.executeTransition(stateName, null, null);
    }
  }

  /**
   * Handle an event This first tries to send the event to the current state, else the catch all
   * will handle the event.
   * 
   * @param event
   * @param parameter
   * @return true if the event was handled
   */
  public boolean handleEvent(String event, Object parameter) {
    this.logDebug(">>'" + event + "' s[" + this.currentState + "] p(" + parameter + ")");

    boolean handled = false;
    String handlerState = null;
    if (this.currentState != null) {
      handlerState = this.currentState;
      handled = this.getState(this.currentState).handleEvent(event, parameter);
    }

    if (handled) {
      this.logDebug("<<'" + event + "' s[" + handlerState + "] p(" + parameter + ")");
    } else {
      // try to handle the event in the catch all state
      handled = this.getState(CATCH_ALL_STATE).handleEvent(event, parameter);

      if (handled) {
        this.logDebug("<<'" + event + "' s[CATCH_ALL_STATE] p(" + parameter + ")");
      } else {
        this.logDebug("<<'" + event + "' s[] p(" + parameter + ")");
      }
    }

    return handled;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("StateMachine(").append(this.getName()).append("){\nStates:\n");
    for (Entry<String, State> state : this.stateMap.entrySet()) {
      sb.append("\t'").append(state.getKey()).append("' - ").append(state.getValue().toString()).append(
          "\n");
    }
    sb.append("}");
    return sb.toString();
  }

  /**
   * Print a debug log entry
   * 
   * @param message
   */
  void logDebug(String message) {
    if (this.detailedLogging) {
      logger.debug(message);
    }
  }

  /**
   * Print a warning log entry
   * 
   * @param message
   * @param t
   */
  void logWarn(String message, Throwable t) {
    if (this.detailedLogging) {
      logger.warn(message);
    }
  }
}
