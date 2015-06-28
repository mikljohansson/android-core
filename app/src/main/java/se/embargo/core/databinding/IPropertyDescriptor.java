package se.embargo.core.databinding;

/**
 * Describes access to a property on an object.
 * @param	<ObjectType>	Type of object to access property on.
 * @param	<ValueType>		Type of value to access.
 */
public interface IPropertyDescriptor<ObjectType, ValueType> {
	/**
	 * Read the property from an object.
	 * @param	object	Object to read from.
	 * @return			The current value of the property.
	 */
	public ValueType getValue(ObjectType object);
	
	/**
	 * Sets the value of the property.
	 * @param	object	Object to write to.
	 * @param	value	Value to set.
	 */
	public void setValue(ObjectType object, ValueType value);
}
