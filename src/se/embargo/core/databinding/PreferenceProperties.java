package se.embargo.core.databinding;

import se.embargo.core.databinding.observable.AbstractObservableValue;
import se.embargo.core.databinding.observable.ChangeEvent;
import se.embargo.core.databinding.observable.ChangeEvent.ChangeType;
import se.embargo.core.databinding.observable.IObservableValue;
import android.content.SharedPreferences;

public class PreferenceProperties {
	public static IValueProperty<SharedPreferences, String> string(final String key, final String defvalue) {
		return new IValueProperty<SharedPreferences, String>() {
			public IObservableValue<String> observe(final SharedPreferences object) {
				return new PreferenceObservable<String>(object, key) {
					public String getValue() {
						return object.getString(key, defvalue);
					}

					public void setValue(String value) {
						SharedPreferences.Editor editor = object.edit();
						editor.putString(key, value);
						editor.commit();
					}
				};
			}
		};
	}
	
	private abstract static class PreferenceObservable<ValueType> extends AbstractObservableValue<ValueType> implements SharedPreferences.OnSharedPreferenceChangeListener {
		private final String _key;
		
		public PreferenceObservable(SharedPreferences prefs, String key) {
			_key = key;
			prefs.registerOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
			if (_key.equals(key)) {
				fireChangeEvent(new ChangeEvent<ValueType>(ChangeType.Reset, getValue()));
			}
		}
	}
}
