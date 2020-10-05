package org.powerbot.script.rt4;

import java.util.HashMap;
import java.util.Map;

/**
 * GeItem
 * Grand Exchange pricing.
 */
public class GeItem extends org.powerbot.script.GeItem {
	static final Map<Integer, Integer> prices = new HashMap<>();

	/**
	 * Creates an instance.
	 *
	 * @param id the item ID
	 * @deprecated use {@link #getPrice(int)} which is now the preferred method
	 */
	@Deprecated
	public GeItem(final int id) {
		super("oldschool", id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.powerbot.script.GeItem nil() {
		return new GeItem(0);
	}

	/**
	 * Finds the price of an item quickly using an internal cache.
	 *
	 * @param id the item ID
	 * @return the lastest cached price, or {@code -1} if the item was not found
	 */
	public static int getPrice(final int id) {
		return prices.getOrDefault(id, -1);
	}
}
