package control.observer;
/**
 * 
 * State machine listener
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
