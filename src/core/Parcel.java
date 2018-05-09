package core;

import java.io.Serializable;

public class Parcel implements Serializable {
	private int ID;
	private String Name;
	private double Area;
	private int PlantID;
	public int getPlantID() {
		return PlantID;
	}

	public void setPlantID(int plantID) {
		PlantID = plantID;
	}

	public Parcel(int id, String name, double area,int plantID) {
		ID = id;
		Name = name;
		Area = area;
		PlantID=plantID;
	}
	public Parcel(int id, String name, double area) {
		ID = id;
		Name = name;
		Area = area;
		PlantID=0;
	}

	public Parcel() {
		ID=-1;
		Name="Unknown";
		Area=-1;
		PlantID=0;
	}

	public int getID() {
		return ID;
	}
	public void setID(int id) {
		ID = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public double getArea() {
		return Area;
	}
	public void setArea(double area) {
		Area = area;
	}
}
