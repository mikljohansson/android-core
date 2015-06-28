package se.embargo.core.databinding.observable;

/**
 * Holds single scalar or object that is observed as a whole.
 * @param <T>	Type of value.
 */
public class WritableValue<T> extends AbstractObservable<T> implements IObservableValue<T> {
	private T _value = null;
	
	/**
	 * Construct an observable where the initial value is null.
	 */
	public WritableValue() {}
	
	/**
	 * @param	value	Initial value to use.
	 */
	public WritableValue(T value) {
		_value = value;
	}

	@Override
	public T getValue() {
		return _value;
	}

	@Override
	public void setValue(T value) {
		_value = value;
		fireChangeEvent(new ChangeEvent<T>(ChangeEvent.ChangeType.Reset, value));
	}
}
