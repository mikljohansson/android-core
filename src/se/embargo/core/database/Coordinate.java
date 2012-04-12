package se.embargo.core.database;

import android.location.Location;

public class Coordinate {
	public static final Coordinate NULL = new Coordinate(0, 0);
	
	private double _latitude;
	private double _longitude;

	public Coordinate(double latitude, double longitude) {
		_latitude = latitude;
		_longitude = longitude;
	}
	
	public static Coordinate create(Location location) {
		if (location != null) {
			return new Coordinate(location.getLatitude(), location.getLongitude());
		}
		
		return NULL;	
	}
	
	public double getLatitude() {
		return _latitude;
	}
	
	public double getLongitude() {
		return _longitude;
	}
}
