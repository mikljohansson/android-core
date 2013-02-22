package se.embargo.core.databinding;

import se.embargo.core.databinding.observable.IObservableValue;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;

public class CursorProperties {
	public static IValueProperty<IObservableValue<Cursor>, ContentValues> values() {
		return new IValueProperty<IObservableValue<Cursor>, ContentValues>() {
			public IObservableValue<ContentValues> observe(final IObservableValue<Cursor> object) {
				return PojoProperties.value(new IPropertyDescriptor<Cursor, ContentValues>() {
					@Override
					public ContentValues getValue(Cursor object) {
						ContentValues values = new ContentValues();
						DatabaseUtils.cursorRowToContentValues(object, values);
						return values;
					}

					@Override
					public void setValue(Cursor object, ContentValues value) {}
				}).observe(object);
			}
		};
	}

	public static IValueProperty<IObservableValue<Cursor>, Integer> integer(final int columnIndex) {
		return new IValueProperty<IObservableValue<Cursor>, Integer>() {
			public IObservableValue<Integer> observe(final IObservableValue<Cursor> object) {
				return PojoProperties.value(new IPropertyDescriptor<Cursor, Integer>() {
					@Override
					public Integer getValue(Cursor object) {
						if (object.moveToFirst()) {
							return object.getInt(columnIndex);
						}
						
						return null;
					}

					@Override
					public void setValue(Cursor object, Integer value) {}
				}).observe(object);
			}
		};
	}
}
