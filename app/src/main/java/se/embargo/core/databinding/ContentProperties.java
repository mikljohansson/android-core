package se.embargo.core.databinding;

import android.content.ContentValues;

/**
 * Observes properties on ContentValues.
 */
public class ContentProperties {
	/**
	 * Accesses a string key.
	 * @param	key	Key or name of column to access.
	 * @return		A property describing access to the key.
	 */
	public static IValueProperty<ContentValues, String> asString(final String key) {
		return new ValueProperty<ContentValues, String>() {
			@Override
			public String getValue(ContentValues object) {
				return object.getAsString(key);
			}

			@Override
			public void setValue(ContentValues object, String value) {
				object.put(key, value);
			}
		};
	}

	/**
	 * Accesses an integer key.
	 * @param	key	Key or name of column to access.
	 * @return		A property describing access to the key.
	 */
	public static IValueProperty<ContentValues, Integer> asInteger(final String key) {
		return new ValueProperty<ContentValues, Integer>() {
			@Override
			public Integer getValue(ContentValues object) {
				return object.getAsInteger(key);
			}

			@Override
			public void setValue(ContentValues object, Integer value) {
				object.put(key, value);
			}
		};
	}

	/**
	 * Accesses a long key.
	 * @param	key	Key or name of column to access.
	 * @return		A property describing access to the key.
	 */
	public static IValueProperty<ContentValues, Long> asLong(final String key) {
		return new ValueProperty<ContentValues, Long>() {
			@Override
			public Long getValue(ContentValues object) {
				return object.getAsLong(key);
			}

			@Override
			public void setValue(ContentValues object, Long value) {
				object.put(key, value);
			}
		};
	}
}
