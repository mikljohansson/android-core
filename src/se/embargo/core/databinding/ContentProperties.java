package se.embargo.core.databinding;

import java.lang.reflect.InvocationTargetException;

import android.content.ContentValues;

public class ContentProperties {
	public static IValueProperty<ContentValues, String> string(final String key) {
		return new ContentProperty<String>(String.class, key);
	}

	public static IValueProperty<ContentValues, Integer> integer(final String key) {
		return new ContentProperty<Integer>(Integer.class, key);
	}
	
	private static class ContentProperty<T> extends ValueProperty<ContentValues, T> {
		private final Class<T> _cls;
		private final String _key;

		public ContentProperty(Class<T> cls, String key) {
			_cls = cls;
			_key = key;
		}

		@Override
		@SuppressWarnings("unchecked")
		public T getValue(ContentValues object) {
			return (T)object.get(_key);
		}

		@Override
		public void setValue(ContentValues object, T value) {
			try {
				object.getClass().getMethod("put", String.class, _cls).invoke(object, _key, value);
			}
			catch (IllegalArgumentException e) {}
			catch (SecurityException e) {}
			catch (IllegalAccessException e) {}
			catch (InvocationTargetException e) {}
			catch (NoSuchMethodException e) {}
		}
	}
}
