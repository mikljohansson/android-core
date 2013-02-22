package se.embargo.core.databinding;

import se.embargo.core.databinding.observable.IObservableValue;
import se.embargo.core.databinding.observable.ObservableContentValue;
import android.content.ContentValues;

public class ContentProperties {
	public static IValueProperty<ContentValues, String> string(final String key) {
		return new IValueProperty<ContentValues, String>() {
			public IObservableValue<String> observe(final ContentValues object) {
				return new ObservableContentValue<String>(String.class, object, key);
			}
		};
	}

	public static IValueProperty<ContentValues, Integer> integer(final String key) {
		return new IValueProperty<ContentValues, Integer>() {
			public IObservableValue<Integer> observe(final ContentValues object) {
				return new ObservableContentValue<Integer>(Integer.class, object, key);
			}
		};
	}
}
