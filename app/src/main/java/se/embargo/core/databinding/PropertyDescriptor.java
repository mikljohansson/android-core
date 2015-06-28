package se.embargo.core.databinding;

/**
 * Default implementation of an IPropertyDescriptor.
 * @param <ObjectType>	Type of object to extract property from.
 * @param <ValueType>	Type of the property value to extract.
 */
public class PropertyDescriptor<ObjectType, ValueType> implements IPropertyDescriptor<ObjectType, ValueType> {
	@Override
	public ValueType getValue(ObjectType object) {
		return null;
	}

	@Override
	public void setValue(ObjectType object, ValueType value) {}
}
