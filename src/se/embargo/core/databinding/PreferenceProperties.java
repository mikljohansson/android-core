package se.embargo.core.databinding;

import se.embargo.core.databinding.observable.AbstractObservableValue;
import se.embargo.core.databinding.observable.ChangeEvent;
import se.embargo.core.databinding.observable.ChangeEvent.ChangeType;
import se.embargo.core.databinding.observable.IObservableValue;
import android.content.SharedPreferences;

/**
 * Observs keys and values in SharedPreferences instances.
 */
public class PreferenceProperties {
	/**
	 * Observe a string preference.
	 * @param key		Key to observe.
	 * @param defvalue	Default value to use in case key isn't set in the preferences.
	 * @return			A property that describes the key.
	 */
	public static IValueProperty<SharedPreferences, String> string(final String key, final String defvalue) {
		return new ValueProperty<SharedPreferences, String>() {
			public IObservableValue<String> observe(final SharedPreferences object) {
				return new PreferenceValue<String>(this, object, key);
			}

			@Override
			public String getValue(android.content.SharedPreferences object) {
				return object.getString(key, defvalue);
			}

			@Override
			public void setValue(android.content.SharedPreferences object, String value) {
				SharedPreferences.Editor editor = object.edit();
				editor.putString(key, value);
				editor.commit();
			}
		};
	}
	
	/**
	 * Observe an int preference.
	 * @param key		Key to observe.
	 * @param defvalue	Default value to use in case key isn't set in the preferences.
	 * @return			A property that describes the key.
	 */
	public static IValueProperty<SharedPreferences, Integer> integer(final String key, final Integer defvalue) {
		return new ValueProperty<SharedPreferences, Integer>() {
			public IObservableValue<Integer> observe(final SharedPreferences object) {
				return new PreferenceValue<Integer>(this, object, key);
			}

			@Override
			public Integer getValue(android.content.SharedPreferences object) {
				return object.getInt(key, defvalue);
			}

			@Override
			public void setValue(android.content.SharedPreferences object, Integer value) {
				SharedPreferences.Editor editor = object.edit();
				editor.putInt(key, value);
				editor.commit();
			}
		};
	}

	/**
	 * Observe an float preference.
	 * @param key		Key to observe.
	 * @param defvalue	Default value to use in case key isn't set in the preferences.
	 * @return			A property that describes the key.
	 */
	public static IValueProperty<SharedPreferences, Float> floating(final String key, final Float defvalue) {
		return new ValueProperty<SharedPreferences, Float>() {
			public IObservableValue<Float> observe(final SharedPreferences object) {
				return new PreferenceValue<Float>(this, object, key);
			}

			@Override
			public Float getValue(android.content.SharedPreferences object) {
				return object.getFloat(key, defvalue);
			}

			@Override
			public void setValue(android.content.SharedPreferences object, Float value) {
				SharedPreferences.Editor editor = object.edit();
				editor.putFloat(key, value);
				editor.commit();
			}
		};
	}
	
	private static class PreferenceValue<ValueType> extends AbstractObservableValue<ValueType> implements SharedPreferences.OnSharedPreferenceChangeListener {
		private final IValueProperty<SharedPreferences, ValueType> _property;
		private final SharedPreferences _object;
		private final String _key;
		
		public PreferenceValue(IValueProperty<SharedPreferences, ValueType> property, SharedPreferences object, String key) {
			_property = property;
			_object = object;
			_key = key;
			_object.registerOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
			if (_key.equals(key)) {
				fireChangeEvent(new ChangeEvent<ValueType>(ChangeType.Reset, getValue()));
			}
		}

		@Override
		public ValueType getValue() {
			return _property.getValue(_object);
		}

		@Override
		public void setValue(ValueType value) {
			_property.setValue(_object, value);
		}
	}
}
