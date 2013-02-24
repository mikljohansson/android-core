package se.embargo.core.databinding;

/**
 * Observes properties of inner observables.
 */
public class PojoProperties {
	/**
	 * Observes an extracted property on an inner observable.
	 * @param	descriptor	Describes the property to extract on the inner observable object.
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
