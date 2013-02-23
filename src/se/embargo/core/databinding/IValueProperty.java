package se.embargo.core.databinding;

import se.embargo.core.databinding.observable.IObservableValue;

public interface IValueProperty<ObjectType, ValueType> extends IPropertyDescriptor<ObjectType, ValueType> {
	public IObservableValue<ValueType> observe(ObjectType object);
	public IObservableValue<ValueType> observe(IObservableValue<ObjectType> object);
}
