package core;

import java.io.Serializable;

public class Base implements Serializable{
	private int id;
	private String name;
	private double latitude;
	private double longitude;
	public Base(int id, String name, double latitude, double longitude) {
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public Base(String name, double latitude, double longitude) {
		this.id = 0;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public Base() {
	}
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String[] toStringArray() {
		String s[]=new String[4];
		s[0]=this.name;
		s[1]=Double.toString(latitude);
		s[2]=Double.toString(longitude);
		s[3]=Integer.toString(id);
		return s;
	}
}
