package se.embargo.core.databinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import se.embargo.core.databinding.observable.ChangeEvent;
import se.embargo.core.databinding.observable.IChangeListener;
import se.embargo.core.databinding.observable.IObservable;
import se.embargo.core.databinding.observable.IObservableValue;

/**
 * Binds observables together and shuffles values to and fro.
 */
public class DataBindingContext {
	@SuppressWarnings("rawtypes")
	private Map<IObservable, List<WeakReference<IChangeListener>>> _connections = new WeakHashMap<IObservable, List<WeakReference<IChangeListener>>>();
	
	/**
	 * Binds two observable values together.
	 * @param	target	Target to transfer value to and from, e.g. a android.view.View observable.
	 * @param	model	Model to transfer value to and from, e.g. a property of a Cursor or ContentValues. 
	 */
	public <T> void bindValue(final IObservableValue<T> target, final IObservableValue<T> model) {
		// Connect the listeners
		connect(target, new ValueChangeDelegate<T>(model));
		connect(model, new ValueChangeDelegate<T>(target));

		// Initialize the target with the model value
		target.setValue(model.getValue());
	}

	/**
	 * Removes all observable bindings in this context
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void dispose() {
		synchronized (_connections) {
			// Disconnect all alive listeners
			for (Map.Entry<IObservable, List<WeakReference<IChangeListener>>> item : _connections.entrySet()) {
				for (WeakReference<IChangeListener> reference : item.getValue()) {
					IChangeListener listener = reference.get();
					if (listener != null) {
						item.getKey().removeChangeListener(listener);
					}
				}
			}
			
			_connections.clear();
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	private <T> void connect(IObservableValue<T> value, IChangeListener<T> listener) {
		synchronized (_connections) {
			// Connect the listener to the value
			value.addChangeListener(listener);
			
			// Add the listener to the list of connections
			List<WeakReference<IChangeListener>> listeners = _connections.get(value);
			if (listeners == null) {
				listeners = new ArrayList<WeakReference<IChangeListener>>();
				_connections.put(value, listeners);
			}
			
			listeners.add(new WeakReference<IChangeListener>(listener));
		}
	}
	
	private static class ValueChangeDelegate<T> implements IChangeListener<T> {
		private IObservableValue<T> _target;
		
		public ValueChangeDelegate(IObservableValue<T> target) {
			_target = target;
		}

		public void handleChange(ChangeEvent<T> event) {
			_target.setValue(event.getValue());
		}
	}
}
