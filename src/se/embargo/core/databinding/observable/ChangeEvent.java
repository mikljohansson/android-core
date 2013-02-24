package se.embargo.core.databinding.observable;

/**
 * Event sent by observables when they change.
 * @param <T>	Type of object that changed.
 */
public class ChangeEvent<T> {
	/**
	 * Type of change that occurred.
	 */
	public enum ChangeType { 
		/**
		 * The whole observable changed, e.g. used by IObservableValue.
		 */
		Reset, 
		
		/**
		 * An entry was added to the observable, e.g. used by IObservableList.
		 */
		Add, 
		
		/**
		 * An entry was removed from the observable, e.g. used by IObservableList.
		 */
		Remove };
	
	/**
	 * Type of change that occurred.
	 */
	private ChangeType _type;
	
	/**
	 * The new value.
	 */
	private T _value;
	
	public ChangeEvent(ChangeType type, T value) {
		_type = type;
		_value = value;
	}
	
	public ChangeEvent() {
		_type = ChangeType.Reset;
		_value = null;
	}

	/**
	 * @return	The type of change that occurred.
	 */
	public ChangeType getType() {
		return _type;
	}
	
	/**
	 * @return	The updated/added/removed value.
	 */
	public T getValue() {
		return _value;
	}
}
