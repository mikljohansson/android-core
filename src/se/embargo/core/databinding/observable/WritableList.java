package se.embargo.core.databinding.observable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Observable list implementation based on wrapping a standard java.util.List.
 * @param <T>	Type of list entry.
 */
public class WritableList<T> extends AbstractObservable<T> implements IObservableList<T> {
	private final List<T> _source;

	public WritableList() {
		this(new ArrayList<T>());
	}
	
	private WritableList(List<T> source) {
		_source = source;
	}

	@Override
	public boolean add(T object) {
		_source.add(object);
		fireChangeEvent(new ChangeEvent<T>(ChangeEvent.ChangeType.Add, object));
		return true;
	}

	@Override
	public void add(int location, T object) {
		_source.add(location, object);
		fireChangeEvent(new ChangeEvent<T>(ChangeEvent.ChangeType.Add, object));
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean addAll(Collection collection) {
		boolean result = _source.addAll(collection);
		if (result) {
			fireChangeEvent(new ChangeEvent<T>());
		}
		
		return result;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean addAll(int location, Collection collection) {
		boolean result = _source.addAll(location, collection);
		if (result) {
			fireChangeEvent(new ChangeEvent<T>());
		}
		
		return result;
	}

	@Override
	public void clear() {
		_source.clear();
		fireChangeEvent(new ChangeEvent<T>());
	}

	@Override
	public boolean contains(Object object) {
		return _source.contains(object);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean containsAll(Collection collection) {
		return _source.containsAll(collection);
	}

	@Override
	public T get(int location) {
		return _source.get(location);
	}

	@Override
	public int indexOf(Object object) {
		return _source.indexOf(object);
	}

	@Override
	public boolean isEmpty() {
		return _source.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return _source.iterator();
	}

	@Override
	public int lastIndexOf(Object object) {
		return _source.lastIndexOf(object);
	}

	@Override
	public ListIterator<T> listIterator() {
		return _source.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int location) {
		return _source.listIterator(location);
	}

	@Override
	public T remove(int location) {
		T result = _source.remove(location);
		fireChangeEvent(new ChangeEvent<T>(ChangeEvent.ChangeType.Remove, result));
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean remove(Object object) {
		boolean result = _source.remove(object);
		if (result) {
			fireChangeEvent(new ChangeEvent<T>(ChangeEvent.ChangeType.Remove, (T)object));
		}
		
		return result;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean removeAll(Collection collection) {
		boolean result = _source.removeAll(collection);
		if (result) {
			fireChangeEvent(new ChangeEvent<T>());
		}
		
		return result;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean retainAll(Collection collection) {
		boolean result = _source.retainAll(collection);
		if (result) {
			fireChangeEvent(new ChangeEvent<T>());
		}
		
		return result;
	}

	@Override
	public T set(int location, T object) {
		T result = _source.set(location, object);
		fireChangeEvent(new ChangeEvent<T>(ChangeEvent.ChangeType.Add, object));
		return result;
	}

	@Override
	public int size() {
		return _source.size();
	}

	@Override
	public List<T> subList(int start, int end) {
		return new WritableList<T>(_source.subList(start, end));
	}

	@Override
	public Object[] toArray() {
		return _source.toArray();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object[] toArray(Object[] array) {
		return _source.toArray(array);
	}
}
