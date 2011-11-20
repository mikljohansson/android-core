package se.embargo.core.databinding.observable;

public class ChangeEvent<T> {
	public enum ChangeType { Reset, Add, Remove };
	
	private ChangeType _type;
	private T _value;
	
	public ChangeEvent(ChangeType type, T value) {
		_type = type;
		_value = value;
	}
	
	public ChangeEvent() {
		_type = ChangeType.Reset;
		_value = null;
	}

	public ChangeType getType() {
		return _type;
	}
	
	public T getValue() {
		return _value;
	}
}
