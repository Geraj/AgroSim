package control.observer;
/**
 * 
 * Interface for fireing state event
 * 
 * @author Geraj
 */
public interface EventProducer {
	/**
	 * Fire state event
	 * @param event
	 * @param parameter
	 * @param source
	 */
	public void fireEvent(StateMachineEvents event, Object parameter, Object source);
}
