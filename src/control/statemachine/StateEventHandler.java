package control.statemachine;

/**
 * Defintion of a state event handler
 * 
 * @author Geraj
 * 
 */
public interface StateEventHandler {
	/**
	 * Handle the event
	 * 
	 * @param parameter
	 */
	public void handleEvent(Object parameter);
}
