package control.statemachine.helper;

/**
 * 
 * @author Geraj
 *
 */
public class WorkingOnParcel {
	/**
	 * 
	 */
	private int parcelId;
	
	/**
	 *  Complete percentage
	 */
	private double complete;

	/**
	 * @param parcelId
	 */
	public WorkingOnParcel(int parcelId, double complete) {
		super();
		this.parcelId = parcelId;
		this.complete = complete;
	}
	
	/**
	 * @return the parcelId
	 */
	public int getParcelId() {
		return parcelId;
	}

	/**
	 * @param parcelId the parcelId to set
	 */
	public void setParcelId(int parcelId) {
		this.parcelId = parcelId;
	}

	/**
	 * @return the complete
	 */
	public double getComplete() {
		return complete;
	}

	/**
	 * @param complete the complete to set
	 */
	public void setComplete(double complete) {
		this.complete = complete;
	}


	
}
