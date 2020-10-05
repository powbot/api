package org.powerbot.script.rt4;

import org.powerbot.script.*;

/**
 * GenericItem
 */
abstract class GenericItem extends Interactive implements Identifiable, Nameable {
	public GenericItem(final ClientContext ctx) {
		super(ctx);
	}

	@Override
	public String name() {
		return CacheItemConfig.load(ctx.bot().getCacheWorker(), id()).name;
	}

	/**
	 * Whether or not the item is a members' item.
	 *
	 * @return {@code true} if it is members only, {@code false} otherwise.
	 */
	public boolean members() {
		return CacheItemConfig.load(ctx.bot().getCacheWorker(), id()).members;
	}

	/**
	 * Whether or not the item is stackable.
	 *
	 * @return {@code true} if the item can be stacked, {@code false} otherwise.
	 */
	public boolean stackable() {
		return CacheItemConfig.load(ctx.bot().getCacheWorker(), id()).stackable;
	}

	/**
	 * Whether or not the item is in banknote form.
	 *
	 * @return {@code true} if it's a banknote, {@code false} otherwise.
	 */
	public boolean noted() {
		return CacheItemConfig.load(ctx.bot().getCacheWorker(), id()).noted;
	}

	/**
	 * Whether or not the item is tradeable.
	 *
	 * @return {@code true} if it is tradeable, {@code false} otherwise.
	 */
	public boolean tradeable() {
		return CacheItemConfig.load(ctx.bot().getCacheWorker(), id()).tradeable;
	}

	/**
	 * Whether or not the item is a cosmetic.
	 *
	 * @return {@code true} if it is a cosmetic, {@code false} otherwise.
	 */
	public boolean cosmetic() {
		return CacheItemConfig.load(ctx.bot().getCacheWorker(), id()).cosmetic;
	}

	/**
	 * The value of the item which would appear in most shops. This may be
	 * used to also grab the high/low alchemy value, as well.
	 *
	 * @return The item value
	 */
	public int value() {
		return CacheItemConfig.load(ctx.bot().getCacheWorker(), id()).value;
	}

	/**
	 * The model ID of the item.
	 *
	 * @return The model id of the item.
	 */
	public int modelId() {
		return CacheItemConfig.load(ctx.bot().getCacheWorker(), id()).modelId;
	}

	/**
	 * An array of the possible actions if the item were on the ground.
	 *
	 * @return A String array.
	 */
	public String[] groundActions() {
		return CacheItemConfig.load(ctx.bot().getCacheWorker(), id()).groundActions;
	}

	/**
	 * An array of the possible actions if the item were in the inventory.
	 *
	 * @return A String array.
	 */
	public String[] inventoryActions() {
		return CacheItemConfig.load(ctx.bot().getCacheWorker(), id()).actions;
	}
}
