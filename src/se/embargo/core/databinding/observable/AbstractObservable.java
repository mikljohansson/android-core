package se.embargo.core.databinding.observable;

import java.util.HashSet;
import java.util.Set;

/**
 * Implements basic event and listener management.
 * @param <T>	Type of object to listen to.
 */
public abstract class AbstractObservable<T> implements IObservable<T> {
	private Set<IChangeListener<T>> _changeListeners = new HashSet<IChangeListener<T>>();

	public void addChangeListener(IChangeListener<T> listener) {
		if (listener != null) {
			synchronized (_changeListeners) {
				_changeListeners.add(listener);
			}
		}
	}

	public void removeChangeListener(IChangeListener<T> listener) {
		if (listener != null) {
			synchronized (_changeListeners) {
				_changeListeners.remove(listener);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void fireChangeEvent(ChangeEvent<T> event) {
		Object[] listeners;
		synchronized (_changeListeners) {
			listeners = _changeListeners.toArray();
		}
			
		for (Object listener : listeners) {
			((IChangeListener<T>)listener).handleChange(event);
		}
	}
}
