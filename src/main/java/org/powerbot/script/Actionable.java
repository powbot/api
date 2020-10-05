package org.powerbot.script;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Actionable
 * An entity which holds an array of actions.
 */
public interface Actionable extends Interactive {
	/**
	 * The current actions for the entity.
	 *
	 * @return the current entity actions
	 */
	String[] actions();

	/**
	 * Query
	 * A base for queries that make use of {@link Actionable} entities.
	 *
	 * @param <T> the type of query to return for chaining
	 */
	interface Query<T> {
		/**
		 * Selects the entities which have one of the specified actions into the query cache.
		 *
		 * @param actions the valid actions
		 * @return {@code this} for the purpose of method chaining
		 */
		default T action(final String... actions) {
			final Pattern[] a = new Pattern[actions.length];
			for (int i = 0; i < actions.length; i++) {
				a[i] = Pattern.compile(Pattern.quote(actions[i]), Pattern.CASE_INSENSITIVE);
			}
			return action(a);
		}

		/**
		 * Selects the entities which have one of the specified actions into the query cache.
		 *
		 * @param actions the valid actions
		 * @return {@code this} for the purpose of method chaining
		 */
		default T action(final Collection<String> actions) {
			return action(actions.toArray(new String[0]));
		}

		/**
		 * Selects the entities which have any action which matches one of the specified action patterns into the query cache.
		 *
		 * @param actions the valid patterns to check RegEx against
		 * @return {@code this} for the purpose of method chaining
		 */
		T action(Pattern... actions);
	}

	/**
	 * Matcher
	 */
	class Matcher implements Filter<Actionable> {
		private final Pattern[] regex;

		public Matcher(final Pattern... actions) {
			regex = actions;
		}

		@Override
		public boolean accept(final Actionable actionable) {
			final String[] actions = actionable.actions();
			if (actions == null || regex == null) {
				return false;
			}
			for (final String action : actions) {
				if (action == null) {
					continue;
				}
				for (final Pattern pattern : regex) {
					if (pattern.matcher(action).matches()) {
						return true;
					}
				}
			}
			return false;
		}
	}
}
