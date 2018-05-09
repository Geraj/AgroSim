package core;

import java.io.Serializable;

public class Operations implements Serializable {
private static final long serialVersionUID = -5377105300934275426L;
private int id;
private String name;
private int price;
private Double time;
public Operations(){
}
public Operations(String name, int price, Double time) {
	super();
	this.id = 0;
	this.name = name;
	this.price = price;
	this.time = time;
}
public Operations(int id,String name, int price, Double time) {
	super();
	this.id = id;
	this.name = name;
	this.price = price;
	this.time = time;
}
public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getPrice() {
	return price;
}
public void setPrice(int price) {
	this.price = price;
}
public Double getTime() {
	return time;
}
public void setTime(Double time) {
	this.time = time;
}
public String[] toStringArrayWithoutID() {
	String s[]=new String[4];
	s[0]=this.name;
	s[1]=Integer.toString(this.price);
	s[2]=Double.toString(this.time);
	return s;
}

}
