package se.embargo.core.databinding.observable;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractObservable<T> implements IObservable<T> {
	private Set<IChangeListener<T>> _changeListeners = new HashSet<IChangeListener<T>>();

	public void addChangeListener(IChangeListener<T> listener) {
		synchronized (_changeListeners) {
			_changeListeners.add(listener);
		}
	}

	public void removeChangeListener(IChangeListener<T> listener) {
		synchronized (_changeListeners) {
			_changeListeners.remove(listener);
		}
	}

	@SuppressWarnings("unchecked")
	protected void fireChangeEvent(ChangeEvent<T> event) {
		synchronized (_changeListeners) {
			for (Object listener : _changeListeners.toArray()) {
				((IChangeListener<T>)listener).handleChange(event);
			}
		}
	}
}
