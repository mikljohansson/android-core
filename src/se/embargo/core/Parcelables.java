package se.embargo.core;

import java.util.Collection;

import android.os.Parcelable;

/**
 * Utilities for working with parcelables.
 */
public abstract class Parcelables {
	public static <T> Parcelable[] toArray(Collection<T> items) {
		Parcelable[] result = new Parcelable[items.size()];
		items.toArray(result);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> void fromArray(Collection<T> result, Parcelable[] items) {
		for (Parcelable item : items) {
			result.add((T)item);
		}
	}
}
