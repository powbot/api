package org.powerbot.script;

/**
 * Textable
 * An entity which contains a text description.
 */
public interface Textable {
	/**
	 * The text description of the entity.
	 *
	 * @return the entity's text description
	 */
	String text();

	/**
	 * Query
	 * A base for queries that make use of {@link Textable} entities.
	 *
	 * @param <T> the type of query to return for chaining
	 */
	interface Query<T> {
		/**
		 * Selects the entities which have a text description that matches any of the specified text descriptions into the query cache.
		 *
		 * @param texts the valid text descriptions
		 * @return {@code this} for the purpose of method chaining
		 */
		T text(String... texts);
	}

	/**
	 * Matcher
	 */
	class Matcher implements Filter<Textable> {
		private final String[] texts;

		public Matcher(final String... texts) {
			this.texts = new String[texts.length];
			for (int i = 0; i < texts.length; i++) {
				this.texts[i] = texts[i].toUpperCase();
			}
		}

		@Override
		public boolean accept(final Textable t) {
			String str = t.text();
			if (str == null) {
				return false;
			}
			str = str.toUpperCase();
			for (final String text : texts) {
				if (text != null && str.contains(text)) {
					return true;
				}
			}
			return false;
		}
	}
}
