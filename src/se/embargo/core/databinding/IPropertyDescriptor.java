package se.embargo.core.databinding;

public interface IPropertyDescriptor<ObjectType, ValueType> {
	public ValueType getValue(ObjectType object);
	public void setValue(ObjectType object, ValueType value);
}
