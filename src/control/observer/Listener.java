package control.observer;
/**
 * 
 * TODO DESCRIPTION
 * 
 * @author Geraj
 */
public interface Listener {
  /**
   * Handle State machine event
   * 
   * @param eventType
   * @param message
   * @param source
   */
	public void handleEvent(StateMachineEvents eventType, Object message, Object source);
}
