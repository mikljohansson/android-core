package se.embargo.core.databinding;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;

public class CursorProperties {
	public static IValueProperty<Cursor, ContentValues> values() {
		return new ValueProperty<Cursor, ContentValues>() {
			@Override
			public ContentValues getValue(Cursor object) {
				ContentValues values = new ContentValues();
				DatabaseUtils.cursorRowToContentValues(object, values);
				return values;
			}

			@Override
			public void setValue(Cursor object, ContentValues value) {}
		};
	}

	public static IValueProperty<Cursor, Integer> integer(final int columnIndex) {
		return new ValueProperty<Cursor, Integer>() {
			@Override
			public Integer getValue(Cursor object) {
				if (object.moveToFirst()) {
					return object.getInt(columnIndex);
				}
				
				return null;
			}

			@Override
			public void setValue(Cursor object, Integer value) {}
		};
	}
}
