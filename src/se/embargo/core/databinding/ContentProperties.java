package se.embargo.core.databinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.ContentValues;
import android.util.Log;

/**
 * Observes properties on ContentValues.
 */
public class ContentProperties {
	private static final String TAG = "ContentProperties";
	
	/**
	 * Accesses a string key.
	 * @param	key	Key or name of column to access.
	 * @return		A property describing access to the key.
	 */
	public static IValueProperty<ContentValues, String> string(final String key) {
		return new ContentProperty<String>(String.class, key);
	}

	/**
	 * Accesses an integer key.
	 * @param	key	Key or name of column to access.
	 * @return		A property describing access to the key.
	 */
	public static IValueProperty<ContentValues, Integer> integer(final String key) {
		return new ContentProperty<Integer>(Integer.class, key);
	}
	
	private static class ContentProperty<T> extends ValueProperty<ContentValues, T> {
		private final Class<T> _cls;
		private final String _key;
		private Method _setter;

		public ContentProperty(Class<T> cls, String key) {
			_cls = cls;
			_key = key;
			
			try {
				_setter = ContentValues.class.getMethod("put", String.class, _cls);
			}
			catch (NoSuchMethodException e) {
				Log.e(TAG, "Illegal value type: " + cls, e);
			}
		}

		@Override
		@SuppressWarnings("unchecked")
		public T getValue(ContentValues object) {
			return (T)object.get(_key);
		}

		@Override
		public void setValue(ContentValues object, T value) {
			if (_setter != null) {
				try {
					_setter.invoke(object, _key, value);
				}
				catch (IllegalArgumentException e) {}
				catch (SecurityException e) {}
				catch (IllegalAccessException e) {}
				catch (InvocationTargetException e) {}
			}
		}
	}
}
