package se.embargo.core.databinding;

public class PropertyDescriptor<ObjectType, ValueType> implements IPropertyDescriptor<ObjectType, ValueType> {
	public ValueType getValue(ObjectType object) {
		return null;
	}

	public void setValue(ObjectType object, ValueType value) {}
}
