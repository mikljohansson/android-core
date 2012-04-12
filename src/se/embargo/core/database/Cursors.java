package se.embargo.core.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class Cursors {
	public static ContentValues getValues(Cursor cursor) {
		ContentValues result = new ContentValues();
		DatabaseUtils.cursorRowToContentValues(cursor, result);
		return result;
	}
	
	public static String getString(Cursor cursor, String columnName) {
		int columnIndex = cursor.getColumnIndex(columnName);
		if (!cursor.isNull(columnIndex)) {
			return cursor.getString(columnIndex);
		}
		
		return "";
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
