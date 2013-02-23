package se.embargo.core.databinding;

import se.embargo.core.databinding.observable.AbstractObservableValue;
import se.embargo.core.databinding.observable.IObservableValue;
import se.embargo.core.databinding.observable.ObservableValueAdapter;

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
		return new ObservableValueAdapter<ObjectType, ValueType>(object) {
			@Override
			public ValueType getValue() {
				ObjectType innerobject = object.getValue();
				if (innerobject != null) {
					return ValueProperty.this.getValue(innerobject);
				}
				
				return null;
			}

			@Override
			public void setValue(ValueType value) {
				ObjectType innerobject = object.getValue();
				if (innerobject != null) {
					ValueProperty.this.setValue(innerobject, value);
				}
			}
		};
	}
}
