package se.embargo.core.concurrent;

/**
 * Logic implementation for use with Parallel.forRange
 * @param <Item>	Type of work item to process
 */
public interface IForBody<Item> {
	/**
	 * Run the calculation on an item over the half open range [it, last)
	 * @param item	The item to process
	 * @param it	Start of range to process
	 * @param last	End offset
	 */
	public void run(Item item, int it, int last);
}
