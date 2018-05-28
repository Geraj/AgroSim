package control.statemachine;

/***
 * Data for event change
 * 
 * @author Geraj
 *
 */
public class MovingToParcel {
	private int from;
	private int to;
		
	/**
	 * @param from
	 * @param to
	 */
	public MovingToParcel(int from, int to) {
		super();
		this.from = from;
		this.to = to;
	}
	/**
	 * @return the from
	 */
	public int getFrom() {
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(int from) {
		this.from = from;
	}
	/**
	 * @return the to
	 */
	public int getTo() {
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(int to) {
		this.to = to;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return " FROM:" + from + " TO:" + to; 
	}

}
