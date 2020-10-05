package org.powerbot.script;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Nameable
 * An entity which has a name.
 */
public interface Nameable {
	/**
	 * The name of the entity.
	 *
	 * @return the entity's name
	 */
	String name();

	/**
	 * Query
	 * A base for queries that make use of {@link Nameable} entities.
	 *
	 * @param <T> the type of query to return for chaining
	 */
	interface Query<T> {
		/**
		 * Selects the entities which have a name that matches any of the specified names into the query cache.
		 *
		 * @param names the valid names
		 * @return {@code this} for the purpose of method chaining
		 */
		default T name(final String... names) {
			final Pattern[] a = new Pattern[names.length];
			for (int i = 0; i < names.length; i++) {
				a[i] = Pattern.compile(Pattern.quote(names[i]), Pattern.CASE_INSENSITIVE);
			}
			return name(a);
		}

		/**
		 * Selects the entities which have a name that matches any of the specified names into the query cache.
		 *
		 * @param names the valid names
		 * @return {@code this} for the purpose of method chaining
		 */
		default T name(final Collection<String> names) {
			return name(names.toArray(new String[0]));
		}

		/**
		 * Selects the entities which have a name that matches any of the specified names into the query cache.
		 *
		 * @param names the valid name arrays to check
		 * @return {@code this} for the purpose of method chaining
		 */
		default T name(final String[]... names) {
			int l = 0, i = 0;
			for (final String[] e : names) {
				l += e.length;
			}
			final String[] a = new String[l];
			for (final String[] e : names) {
				for (final String x : e) {
					a[i++] = x;
				}
			}
			return name(a);
		}

		/**
		 * Selects the entities which have a name that matches one of the specified action patterns into the query cache.
		 *
		 * @param names the valid patterns to check RegEx against
		 * @return {@code this} for the purpose of method chaining
		 */
		T name(Pattern... names);

		/**
		 * Selects the entities which have a name that matches any of the specified nameables names into the query cache.
		 *
		 * @param names the valid nameables to check against
		 * @return {@code this} for the purpose of method chaining
		 */
		default T name(final Nameable... names) {
			final String[] a = new String[names.length];
			for (int i = 0; i < names.length; i++) {
				a[i] = names[i].name();
			}
			return name(a);
		}
	}

	/**
	 * Matcher
	 */
	class Matcher implements Filter<Nameable> {
		private final Pattern[] regex;

		public Matcher(final Pattern... names) {
			regex = names;
		}

		@Override
		public boolean accept(final Nameable nameable) {
			final String n = nameable.name();
			if (n == null || regex == null) {
				return false;
			}
			for (final Pattern pattern : regex) {
				if (pattern != null && pattern.matcher(n).matches()) {
					return true;
				}
			}
			return false;
		}
	}
}
