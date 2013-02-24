package se.embargo.core.databinding;

import se.embargo.core.databinding.observable.IObservableValue;

/**
 * Describes an observable property.
 * @param	<ObjectType>	Type of object to access property on.
 * @param	<ValueType>		Type of value to access.
 */
public interface IValueProperty<ObjectType, ValueType> extends IPropertyDescriptor<ObjectType, ValueType> {
	/**
	 * Observes this property on an object.
	 * @param	object	Object to access.
	 * @return			An observable value for this property on the given object.
	 */
	public IObservableValue<ValueType> observe(ObjectType object);

	/**
	 * Observes this property on an object.
	 * @param	object	Observable holding the object to access.
	 * @return			An observable value for this property on the given object.
	 */
	public IObservableValue<ValueType> observe(IObservableValue<ObjectType> object);
}
