package se.embargo.core.databinding.observable;

import java.lang.reflect.InvocationTargetException;

import android.content.ContentValues;

public class ObservableContentValue<T> extends AbstractObservableValue<T> {
	private Class<T> _cls;
	private ContentValues _values;
	private String _key;

	public ObservableContentValue(Class<T> cls, ContentValues values, String key) {
		_cls = cls;
		_values = values;
		_key = key;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T getValue() {
		return (T)_values.get(_key);
	}

	@Override
	public void setValue(T value) {
		try {
			_values.getClass().getMethod("put", String.class, _cls).invoke(_values, _key, value);
		}
		catch (IllegalArgumentException e) {}
		catch (SecurityException e) {}
		catch (IllegalAccessException e) {}
		catch (InvocationTargetException e) {}
		catch (NoSuchMethodException e) {}
		
		fireChangeEvent(new ChangeEvent<T>(ChangeEvent.ChangeType.Reset, value));
	}
}
