package control.statemachine;

/**
 * 
 * @author Geraj
 * 
 */
public interface TransitionCondition {
	/**
	 * Called to check if a transition should be executed or not
	 * 
	 * @param parameter
	 * @return true if condition should be executed
	 */
	boolean isAllowed(Object parameter);
}
