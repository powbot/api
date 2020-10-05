package org.powerbot.script;

import java.util.Comparator;

/**
 * Locatable
 * An entity located within the Runescape world map.
 */
public interface Locatable {
	/**
	 * The current location of the entity.
	 *
	 * @return the entity's current location
	 */
	Tile tile();

	/**
	 * Query
	 * A base for queries that make use of {@link Locatable} entities.
	 *
	 * @param <T> the type of query to return for chaining
	 */
	interface Query<T> {
		/**
		 * Selects the entities which are located on the specified tile of the locatable into the query cache.
		 *
		 * @param t the locatable to check tile against
		 * @return {@code this} for the purpose of method chaining
		 */
		T at(Locatable t);

		/**
		 * Selects the entities which are located within the specified radius of the local player into the query cache.
		 *
		 * @param radius the restriction radius
		 * @return {@code this} for the purpose of method chaining
		 */
		T within(double radius);

		/**
		 * Selects the entities which are located within the specified radius of the locatable into the query cache.
		 *
		 * @param locatable the locatable to base the restriction radius around
		 * @param radius    the restriction radius
		 * @return {@code this} for the purpose of method chaining
		 */
		T within(Locatable locatable, double radius);

		/**
		 * Selects the entities which are located within the area into the query cache.
		 *
		 * @param area the restriction area
		 * @return {@code this} for the purpose of method chaining
		 */
		T within(Area area);

		/**
		 * Sorts the query cache by ascending order by distance to the local player.
		 *
		 * @return {@code this} for the purpose of method chaining
		 */
		T nearest();

		/**
		 * Sorts the query cache by ascending order by distance to the specified locatable.
		 *
		 * @param locatable the locatable to check distances from
		 * @return {@code this} for the purpose of method chaining
		 */
		T nearest(Locatable locatable);
	}

	/**
	 * Matcher
	 */
	class Matcher implements Filter<Locatable> {
		private final Tile target;

		public Matcher(final Locatable target) {
			this.target = target.tile();
		}

		@Override
		public boolean accept(final Locatable locatable) {
			final Tile tile = locatable != null ? locatable.tile() : null;
			return target != null && target.equals(tile);
		}
	}

	/**
	 * WithinRange
	 */
	class WithinRange implements Filter<Locatable> {
		private final Tile target;
		private final double distance;

		public WithinRange(final Locatable target, final double distance) {
			this.target = target.tile();
			this.distance = distance;
		}

		@Override
		public boolean accept(final Locatable l) {
			final Tile tile = l != null ? l.tile() : null;
			return tile != null && target != null && tile.distanceTo(target) <= distance;
		}
	}

	/**
	 * WithinArea
	 */
	class WithinArea implements Filter<Locatable> {
		private final Area area;

		public WithinArea(final Area area) {
			this.area = area;
		}

		@Override
		public boolean accept(final Locatable l) {
			final Tile tile = l != null ? l.tile() : null;
			return tile != null && area.contains(tile);
		}
	}

	/**
	 * NearestTo
	 */
	class NearestTo implements Comparator<Locatable> {
		private final Tile target;

		public NearestTo(final Locatable target) {
			this.target = target.tile();
		}

		@Override
		public int compare(final Locatable o1, final Locatable o2) {
			final Tile t1 = o1.tile();
			final Tile t2 = o2.tile();
			if (target == null || t1 == null || t2 == null) {
				return Integer.MAX_VALUE;
			}
			final double d1 = t1.distanceTo(target);
			final double d2 = t2.distanceTo(target);
			return Double.compare(d1, d2);
		}
	}
}
