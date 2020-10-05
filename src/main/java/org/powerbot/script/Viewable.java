package org.powerbot.script;

/**
 * Viewable
 * An object which can be rendered in the viewport.
 */
public interface Viewable {
	/**
	 * Returns {@code true} if the object is currently rendered in the viewport.
	 *
	 * @return {@code true} if the object is currently rendered in the viewport, otherwise {@code false}.
	 */
	boolean inViewport();

	/**
	 * Query
	 * A base for queries that make use of {@link Viewable} entities.
	 *
	 * @param <T> the type of query to return for chaining
	 */
	interface Query<T> {
		/**
		 * Selects the entities which are present in the viewport into the query cache.
		 *
		 * @return {@code this} for the purpose of method chaining
		 */
		T viewable();
	}
}
