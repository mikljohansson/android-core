package se.embargo.core.databinding;

import se.embargo.core.databinding.observable.IObservableValue;
import se.embargo.core.databinding.observable.ObservableValueAdapter;

public class PojoProperties {
	public static <ObjectType, ValueType> IValueProperty<IObservableValue<ObjectType>, ValueType> value(final IPropertyDescriptor<ObjectType, ValueType> descriptor) {
		return new IValueProperty<IObservableValue<ObjectType>, ValueType>() {
			public IObservableValue<ValueType> observe(final IObservableValue<ObjectType> object) {
				return new ObservableValueAdapter<ObjectType, ValueType>(object) {
					public ValueType getValue() {
						ObjectType obj = object.getValue();
						if (obj != null) {
							return descriptor.getValue(obj);
						}
						
						return null;
					}

					public void setValue(ValueType value) {
						ObjectType obj = object.getValue();
						if (obj != null) {
							descriptor.setValue(obj, value);
						}
					}
				};
			}
		};
	}
}
