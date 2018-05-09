package core;

import java.io.Serializable;

public class Points implements Serializable{
private int ID;
private int ParcelID;
private double Longitude;
private double Latitude;
private double Elevation;

public Points(int id, int parcelID, double latitude, double longitude,
		double elevation) {
	ID = id;
	ParcelID = parcelID;
	Longitude = longitude;
	Latitude = latitude;
	Elevation = elevation;
}

public int getID() {
	return ID;
}
public void setID(int id) {
	ID = id;
}
public int getParcelID() {
	return ParcelID;
}
public void setParcelID(int parcelID) {
	ParcelID = parcelID;
}
public double getLongitude() {
	return Longitude;
}
public void setLongitude(double longitude) {
	Longitude = longitude;
}
public double getLatitude() {
	return Latitude;
}
public void setLatitude(double latitude) {
	Latitude = latitude;
}
public double getElevation() {
	return Elevation;
}
public void setElevation(double elevation) {
	Elevation = elevation;
}

}
