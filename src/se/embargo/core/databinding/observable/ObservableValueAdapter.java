package se.embargo.core.databinding.observable;

public abstract class ObservableValueAdapter<ObjectType, ValueType> extends AbstractObservableValue<ValueType> {
	private IObservable<ObjectType> _object;
	private IChangeListener<ObjectType> _listener = new ListenerAdapter();
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
	
	private class ListenerAdapter implements IChangeListener<ObjectType> {
		public void handleChange(ChangeEvent<ObjectType> event) {
			fireChangeEvent(new ChangeEvent<ValueType>(ChangeEvent.ChangeType.Reset, getValue()));
		}
	}
}
