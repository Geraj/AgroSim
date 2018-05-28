package control.observer;
/**
 * 
 * TODO DESCRIPTION
 * 
 * @author Geraj
 */
public enum StateMachineEvents {
	/***/
	TICK("Time changed"),
	/***/
	VEHICLE_MOVING_TO_PARCEL("Moving to destination"),
	/***/
	VEHICLE_STARTED ("Started"),
	/***/
	VEHICLE_STOPPED ("Stopped"),
	/***/
	VEHICLE_WORKING ("Working");
	/***/
	private String description;
	/**Constructor 
	 * @param description */
	StateMachineEvents(String description) {
		this.description = description;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}
