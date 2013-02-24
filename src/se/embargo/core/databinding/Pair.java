package se.embargo.core.databinding;

/**
 * A pair of values.
 * @param	<FirstType>		Type of first value.
 * @param	<SecondType>	Type of second value.
 */
public class Pair<FirstType, SecondType> {
	public final FirstType first;
	public final SecondType second;
	
	public Pair(FirstType first, SecondType second) {
		this.first = first;
		this.second = second;
	}
}
