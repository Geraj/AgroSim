package control.observer;
/**
 * 
 * TODO DESCRIPTION
 * 
 * @author Geraj
 */
public interface EventProducer {
	public void fireEvent(StateMachineEvents event, Object parameter, Object source);
}
