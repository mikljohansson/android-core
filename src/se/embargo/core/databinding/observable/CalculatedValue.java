package se.embargo.core.databinding.observable;

public abstract class CalculatedValue<ObjectType, ValueType> extends ObservableValueAdapter<ObjectType, ValueType> {
	public CalculatedValue(IObservable<ObjectType> object) {
		super(object);
	}

	@Override
	public final void setValue(ValueType value) {}
}
