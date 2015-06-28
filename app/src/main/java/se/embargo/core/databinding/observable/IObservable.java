package se.embargo.core.databinding.observable;

/**
 * Interface for observable pattern. 
 * @param <T>	Type of object to listen to.
 */
public interface IObservable<T> {
	/**
	 * Attach a listener to notify when this observable changes.
	 * @param	listener	Listener to attach.
	 */
	public void addChangeListener(IChangeListener<T> listener);
	
	/**
	 * Detach a listener from this observable.
	 * @param	listener	Listener to detach.
	 */
	public void removeChangeListener(IChangeListener<T> listener);
}
