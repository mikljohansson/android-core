package se.embargo.core.database;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class Cursors {
	public static String getString(Cursor cursor, String columnName) {
		int columnIndex = cursor.getColumnIndex(columnName);
		if (!cursor.isNull(columnIndex)) {
			return cursor.getString(columnIndex);
		}
		
		return "";
	}

	public static long getLong(Cursor cursor, String columnName) {
		int columnIndex = cursor.getColumnIndex(columnName);
		if (!cursor.isNull(columnIndex)) {
			return cursor.getLong(columnIndex);
		}
		
		return 0;
	}

	public static byte[] getBlob(Cursor cursor, String columnName) {
		int columnIndex = cursor.getColumnIndex(columnName);
		if (!cursor.isNull(columnIndex)) {
			return cursor.getBlob(columnIndex);
		}
		
		return new byte[] {};
	}
	
	public static Coordinate getCoordinate(Cursor cursor, String latitude, String longitude) {
		int latitudeIndex = cursor.getColumnIndex(latitude),
			longitudeIndex = cursor.getColumnIndex(longitude);
		
		if (!cursor.isNull(latitudeIndex) && !cursor.isNull(longitudeIndex)) {
			return new Coordinate(cursor.getInt(latitudeIndex), cursor.getInt(longitudeIndex));
		}
		
		return Coordinate.NULL;
	}
	
	public static Bitmap getBitmap(Cursor cursor, String columnName) {
		int columnIndex = cursor.getColumnIndex(columnName);
		if (!cursor.isNull(columnIndex)) {
			byte[] content = cursor.getBlob(columnIndex);
			return BitmapFactory.decodeByteArray(content, 0, content.length);
		}
		
		return null;
	}
	
	public static Bitmap getBitmap(Cursor cursor, String columnName, int width, int height) {
		Bitmap bm = getBitmap(cursor, columnName);
		if (bm != null) {
			return Bitmap.createScaledBitmap(bm, width, height, true);
		}
		
		return null;
	}
}
