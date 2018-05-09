package core;

import java.io.Serializable;

/**
 * TODO DESCRIPTION
 * 
 * @author Geraj
 */
public class Path implements Serializable {
  
	private int id;
	private int from;
	private int to;
	private double length;
	
	
	/**
	 * Constructs a new instance.
	 * @param id
	 * @param from
	 * @param to
	 * @param length
	 */
	public Path(int id, int from, int to, double length) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.length = length;
	}
	/**
	 * Constructs a new instance.
	 */
	public Path(){
		id=-1;
		from=0;
		to=0;
		length=0;
	}
	/**
	 * TODO DESCRIPTION
	 * @return
	 */
	public int getID() {
		return id;
	}
	/**
	 * TODO DESCRIPTION
	 * @param id
	 */
	public void setID(int id) {
		this.id = id;
	}
	/**
	 * TODO DESCRIPTION
	 * @return
	 */
	public int getFrom() {
		return from;
	}
	/**
	 * TODO DESCRIPTION
	 * @param from
	 */
	public void setFrom(int from) {
		this.from = from;
	}
	/**
	 * TODO DESCRIPTION
	 * @return
	 */
	public int getTo() {
		return to;
	}
	/**
	 * TODO DESCRIPTION
	 * @param to
	 */
	public void setTo(int to) {
		this.to = to;
	}
	/**
	 * TODO DESCRIPTION
	 * @return
	 */
	public double getLength() {
		return length;
	}
	/**
	 * TODO DESCRIPTION
	 * @param length
	 */
	public void setLength(double length) {
		this.length = length;
	}

}
