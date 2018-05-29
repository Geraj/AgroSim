package control.statemachine.helper;

import java.util.ArrayList;

/***
 * Data for event change
 * 
 * @author Geraj
 *
 */
public class MovingToParcel {
	private int from;
	private int to;
	private int timeToTravel;
	private ArrayList<Integer> pathToTravel = new ArrayList<>();
		
	/**
	 * @param from
	 * @param to
	 * @param timetotravel 
	 */
	public MovingToParcel(int from, int to, int timetotravel, ArrayList<Integer> pathToTravel) {
		super();
		this.from = from;
		this.to = to;
		this.timeToTravel = timetotravel;
		this.pathToTravel = pathToTravel;
	}
	/**
	 * @return the pathToTravel
	 */
	public ArrayList<Integer> getPathToTravel() {
		return pathToTravel;
	}

	/**
	 * @return the from
	 */
	public int getFrom() {
		return from;
	}

	/**
	 * @return the timeToTravel
	 */
	public int getTimeToTravel() {
		return timeToTravel;
	}
	/**
	 * @return the to
	 */
	public int getTo() {
		return to;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return " FROM:" + from + " TO:" + to; 
	}

}
