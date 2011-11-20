package se.embargo.core.databinding.observable;

public class WritableValue<T> extends AbstractObservable<T> implements IObservableValue<T> {
	private T _value = null;
	
	public WritableValue() {}
	
	public WritableValue(T value) {
		_value = value;
	}

	public T getValue() {
		return _value;
	}

	public void setValue(T value) {
		_value = value;
		fireChangeEvent(new ChangeEvent<T>(ChangeEvent.ChangeType.Reset, value));
	}
}
