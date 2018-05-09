package core;

import java.io.Serializable;

public class PathPoints implements Serializable {
		private int ID;
		private int PathID;
		private double Longitude;
		private double Latitude;
		private double Elevation;

		public PathPoints(int id, int parcelID, double latitude, double longitude,
				double elevation) {
			ID = id;
			PathID = parcelID;
			Longitude = longitude;
			Latitude = latitude;
			Elevation = elevation;
		}
		public PathPoints(double latitude, double longitude) {
			ID = -1;
			PathID = -1;
			Longitude = longitude;
			Latitude = latitude;
			Elevation = -1;
		}

		public int getID() {
			return ID;
		}
		public void setID(int id) {
			ID = id;
		}
		public int getParcelID() {
			return PathID;
		}
		public void setParcelID(int parcelID) {
			PathID = parcelID;
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
