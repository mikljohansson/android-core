package se.embargo.core.databinding;

import se.embargo.core.databinding.observable.IObservableValue;

public interface IValueProperty<ObjectType, ValueType> {
	public IObservableValue<ValueType> observe(ObjectType object);
}
