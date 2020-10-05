package org.powerbot.script;

/**
 * Stackable
 * An item which has a variable quantity.
 */
public interface Stackable {
	/**
	 * The quantity of the item.
	 *
	 * @return the item's quantity
	 */
	int stackSize();

	/**
	 * Query
	 * A base for queries that make use of {@link Stackable} entities.
	 *
	 * @param <T> the type of query to return for chaining
	 */
	interface Query<T> {
		/**
		 * Retrieves the count of the query cache, ignoring stack sizes.
		 *
		 * @return the query cache count
		 */
		int count();

		/**
		 * Retrieves the count of the query, possibly including stack sizes.
		 *
		 * @param stacks whether or not to count stack sizes
		 * @return the query cache count, possibly including stack sizes
		 */
		int count(boolean stacks);
	}
}
