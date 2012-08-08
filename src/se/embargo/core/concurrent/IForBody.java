package se.embargo.core.concurrent;

/**
 * Logic implementation for use with Parallel.For
 * @param <T>	Type of work item to process
 */
public interface IForBody<T> {
	/**
	 * Run the calculation on an item over the half open range [it, last)
	 * @param item	The item to process
	 * @param it	Start of range to process
	 * @param last	End offset
	 */
	public void run(T item, int it, int last);
}
