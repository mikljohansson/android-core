package se.embargo.core.databinding;

import se.embargo.core.databinding.observable.IObservableValue;

public interface IDataBindingContext {
	public <T> void bindValue(IObservableValue<T> lhs, IObservableValue<T> rhs);
	public void dispose();
}
