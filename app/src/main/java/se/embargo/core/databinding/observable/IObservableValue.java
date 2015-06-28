package se.embargo.core.databinding.observable;

/**
 * Holds single scalar or object that is observed as a whole.
 * @param <T>	Type of value.
 */
public interface IObservableValue<T> extends IObservable<T> {
	/**
	 * @return	The current value.
	 */
	public T getValue();
	
	/**
	 * Changes the current value.
	 * @param	value	Value to set.	
	 */
	public void setValue(T value);
}
