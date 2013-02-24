package se.embargo.core.databinding.observable;

import java.util.List;

/**
 * A java.util.List that supports change notification.
 * @param <T>	Type of list entry.
 */
public interface IObservableList<T> extends IObservable<T>, List<T> {}
