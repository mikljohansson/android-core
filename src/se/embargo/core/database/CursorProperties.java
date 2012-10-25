package se.embargo.core.database;

import se.embargo.core.databinding.IPropertyDescriptor;
import se.embargo.core.databinding.IValueProperty;
import se.embargo.core.databinding.PojoProperties;
import se.embargo.core.databinding.observable.IObservableValue;
import android.database.Cursor;

public class CursorProperties {
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
