package org.powerbot.script;

/**
 * Identifiable
 * An entity which has an identifier.
 */
public interface Identifiable {
	/**
	 * The identifier of this entity
	 *
	 * @return the entity identifier
	 */
	int id();

	/**
	 * Query
	 * A base for queries that make use of {@link Identifiable} entities.
	 *
	 * @param <T> the type of query to return for chaining
	 */
	interface Query<T> {
		/**
		 * Selects the entities which have one of the provided ids into the query cache.
		 *
		 * @param ids the valid ids
		 * @return {@code this} for the purpose of method chaining
		 */
		T id(int... ids);

		/**
		 * Selects the entities which have one of the provided ids into the query cache.
		 *
		 * @param ids the valid id arrays to check
		 * @return {@code this} for the purpose of method chaining
		 */
		default T id(final int[]... ids) {
			int l = 0, i = 0;
			for (final int[] e : ids) {
				l += e.length;
			}
			final int[] a = new int[l];
			for (final int[] e : ids) {
				for (final int x : e) {
					a[i++] = x;
				}
			}
			return id(a);
		}

		/**
		 * Selects the entities which have one of the provided ids into the query cache.
		 *
		 * @param ids the valid identifiables to check their ids against
		 * @return {@code this} for the purpose of method chaining
		 */
		default T id(final Identifiable... ids) {
			final int[] a = new int[ids.length];
			for (int i = 0; i < ids.length; i++) {
				a[i] = ids[i].id();
			}
			return id(a);
		}
	}

	/**
	 * Matcher
	 */
	class Matcher implements Filter<Identifiable> {
		private final int[] ids;

		public Matcher(final int... ids) {
			this.ids = ids;
		}

		@Override
		public boolean accept(final Identifiable identifiable) {
			final int x = identifiable != null ? identifiable.id() : -1;
			if (x == -1) {
				return false;
			}
			for (final int id : ids) {
				if (id == x) {
					return true;
				}
			}
			return false;
		}
	}
}
