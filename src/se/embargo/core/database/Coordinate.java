package se.embargo.core.database;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Coordinate implements Parcelable {
	public static final Coordinate NULL = new Coordinate(0, 0);
	
	private final double _latitude;
	private final double _longitude;

	public Coordinate(double latitude, double longitude) {
		_latitude = latitude;
		_longitude = longitude;
	}

	public Coordinate(Location location) {
		if (location != null) {
			_latitude = location.getLatitude();
			_longitude = location.getLongitude();
		}
		else {
			_latitude = 0;
			_longitude = 0;
		}
	}
	
	public double getLatitude() {
		return _latitude;
	}
	
	public double getLongitude() {
		return _longitude;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(_latitude);
		dest.writeDouble(_longitude);
	}
	
	public static final Parcelable.Creator<Coordinate> CREATOR = new Parcelable.Creator<Coordinate>() {
		public Coordinate createFromParcel(Parcel in) {
			double latitude = in.readDouble(), longitude = in.readDouble();
			return new Coordinate(latitude, longitude);
		}
		
		public Coordinate[] newArray(int size) {
			return new Coordinate[size];
		}
	};
}
