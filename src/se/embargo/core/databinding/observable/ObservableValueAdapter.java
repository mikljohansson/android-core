package se.embargo.core.databinding.observable;

/**
 * Wraps an observable and proxies change notifications from it.
 * @param <ObjectType>	Type of inner observable.
 * @param <ValueType>	Result type of this observable value.
 */
public abstract class ObservableValueAdapter<ObjectType, ValueType> extends AbstractObservableValue<ValueType> {
	private final IObservable<ObjectType> _object;
	private final IChangeListener<ObjectType> _listener = new ListenerAdapter();
	private long _listeners = 0;
	
	public ObservableValueAdapter(IObservable<ObjectType> object) {
		_object = object;
	}
	
	@Override
	public synchronized void addChangeListener(IChangeListener<ValueType> listener) {
		if (listener != null) {
			super.addChangeListener(listener);
			
			if (_listeners++ == 0) {
				_object.addChangeListener(_listener);
			}
		}
	}

	@Override
	public synchronized void removeChangeListener(IChangeListener<ValueType> listener) {
		if (listener != null) {
			super.removeChangeListener(listener);

			if (--_listeners == 0) {
				_object.removeChangeListener(_listener);
			}
		}
	}
	
	/**
	 * Handle a change of the inner observable.
	 * @param event	Change event propagated from the inner observable.
	 */
	protected void handleObservableChanged(ChangeEvent<ObjectType> event) {
		fireChangeEvent(new ChangeEvent<ValueType>(ChangeEvent.ChangeType.Reset, getValue()));
	}
	
	private class ListenerAdapter implements IChangeListener<ObjectType> {
		public void handleChange(ChangeEvent<ObjectType> event) {
			handleObservableChanged(event);
		}
	}
}
