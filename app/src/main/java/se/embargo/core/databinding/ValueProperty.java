package se.embargo.core.databinding;

import se.embargo.core.databinding.observable.AbstractObservableValue;
import se.embargo.core.databinding.observable.ChangeEvent;
import se.embargo.core.databinding.observable.IObservableValue;
import se.embargo.core.databinding.observable.ObservableValueAdapter;

/**
 * Implements the observe() methods using the IPropertyDescriptor base interface.
 */
abstract class ValueProperty<ObjectType, ValueType> implements IValueProperty<ObjectType, ValueType> {
	@Override
	public IObservableValue<ValueType> observe(final ObjectType object) {
		return new AbstractObservableValue<ValueType>() {
			@Override
			public ValueType getValue() {
				return ValueProperty.this.getValue(object);
			}

			@Override
			public void setValue(ValueType value) {
				ValueProperty.this.setValue(object, value);
			}
		};
	}

	@Override
	public IObservableValue<ValueType> observe(final IObservableValue<ObjectType> object) {
		return new ValueAdapter(object);
	}
	
	private class ValueAdapter extends ObservableValueAdapter<ObjectType, ValueType> {
		private IObservableValue<ValueType> _value = null;
		
		public ValueAdapter(IObservableValue<ObjectType> object) {
			super(object);
			
			ObjectType innerobject = object.getValue();
			if (innerobject != null) {
				_value = observe(innerobject);
			}
		}

		@Override
		public ValueType getValue() {
			if (_value != null) {
				return _value.getValue();
			}
			
			return null;
		}

		@Override
		public void setValue(ValueType value) {
			if (_value != null) {
				_value.setValue(value);
			}
		}
		
		@Override
		protected void handleObservableChanged(ChangeEvent<ObjectType> event) {
			ObjectType innerobject = event.getValue();
			if (innerobject != null) {
				_value = observe(innerobject);
			}
			else {
				_value = null;
			}
			
			super.handleObservableChanged(event);
		}
	}
}
