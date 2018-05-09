package core;

import java.io.Serializable;

public class Plants implements Serializable{
private int ID;
private String Type;
private int Seed;
private int Income;


public Plants(int id, String type, int seed, int income) {
	ID = id;
	Type = type;
	Seed = seed;
	Income = income;
}
public int getID() {
	return ID;
}
public void setID(int id) {
	ID = id;
}
public String getType() {
	return Type;
}
public void setType(String type) {
	Type = type;
}
public int getSeed() {
	return Seed;
}
public void setSeed(int seed) {
	Seed = seed;
}
public int getIncome() {
	return Income;
}
public void setIncome(int income) {
	Income = income;
}
public String[] toStringArray() {
	String s[]=new String[4];
	s[0]=Integer.toString(this.ID);
	s[1]=this.Type;
	s[2]=Integer.toString(this.Seed);
	s[3]=Integer.toString(this.Income);
	return s;
}
public String[] toStringArrayWithoutID() {
	String s[]=new String[4];
	s[0]=this.Type;
	s[1]=Integer.toString(this.Seed);
	s[2]=Integer.toString(this.Income);
	s[3]=Integer.toString(this.ID);
	return s;
}

}
