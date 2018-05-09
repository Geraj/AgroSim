package core;

import java.io.Serializable;

public class Machines implements Serializable {
private String id;
private String type;
private int speed;
private double workspeed;

public Machines(){
}
public Machines(String id, String type, int speed, double workspeed) {
	super();
	this.id = id;
	this.type = type;
	this.speed = speed;
	this.workspeed = workspeed;
}
public String getID() {
	return id;
}
public void setID(String id) {
	this.id = id;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public int getSpeed() {
	return speed;
}
public void setSpeed(int speed) {
	this.speed = speed;
}
public double getWorkspeed() {
	return workspeed;
}
public void setWorkspeed(double workspeed) {
	this.workspeed = workspeed;
}
public String[] toStringArray() {
	String s[]=new String[4];
	s[0]=this.id;
	s[1]=this.type;
	s[2]=Integer.toString(this.speed);
	s[3]=Double.toString(this.workspeed);
	return s;
}


}
