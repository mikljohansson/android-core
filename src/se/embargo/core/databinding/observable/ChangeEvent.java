package se.embargo.core.databinding.observable;

public class ChangeEvent<T> {
	private T _value;
	
	public ChangeEvent(T value) {
		_value = value;
	}
	
	public T getValue() {
		return _value;
	}
}
