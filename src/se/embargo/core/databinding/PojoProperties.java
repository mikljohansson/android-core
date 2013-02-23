package se.embargo.core.databinding;


public class PojoProperties {
	/**
	 * Observes the current value of an IObservableValue
	 */
	public static <ObjectType, ValueType> IValueProperty<ObjectType, ValueType> value(final IPropertyDescriptor<ObjectType, ValueType> descriptor) {
		return new ValueProperty<ObjectType, ValueType>() {
			public ValueType getValue(ObjectType object) {
				return descriptor.getValue(object);
			}

			public void setValue(ObjectType object, ValueType value) {
				descriptor.setValue(object, value);
			}
		};
	}
}
