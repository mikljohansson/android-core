package se.embargo.core.concurrent;

/**
 * Logic implementation for use with Parallel.mapReduce
 * @param <Item>	Type of work item to process
 */
public interface MapReduceBody<Item, Result> {
	/**
	 * Run the calculation on an item over the half open range [it, last)
	 * @param item	The item to process
	 * @param it	Start of range to process
	 * @param last	End offset
	 */
	public Result map(Item item, int it, int last);
	
	/**
	 * Merge two partial results
	 * @param lhs	Left result to merge
	 * @param rhs	Right result to merge
	 * @return		Merged result, may be the same object as either lhs or rhs
	 */
	public Result reduce(Result lhs, Result rhs);
}
