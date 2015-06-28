package se.embargo.core.databinding;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;

/**
 * Observes properties of cursors.
 */
public class CursorProperties {
	/**
	 * @return	A property that extracts a cursor/row into a ContentValues object.
	 */
	public static IValueProperty<Cursor, ContentValues> values() {
		return new ValueProperty<Cursor, ContentValues>() {
			@Override
			public ContentValues getValue(Cursor object) {
				if (object.moveToFirst()) {
					ContentValues values = new ContentValues();
					DatabaseUtils.cursorRowToContentValues(object, values);
					return values;
				}
				
				return null;
			}

			@Override
			public void setValue(Cursor object, ContentValues value) {}
		};
	}

	/**
	 * Accesses an integer column of a cursor.
	 * @param	columnIndex	Index of column to access.
	 * @return				A property describing access to the column.
	 */
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
