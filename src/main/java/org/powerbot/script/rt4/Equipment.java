package org.powerbot.script.rt4;

import java.util.ArrayList;
import java.util.List;

/**
 * Equipment
 * A utility class for interacting with worn items on the player.
 */
public class Equipment extends ItemQuery<Item> {
	public Equipment(final ClientContext factory) {
		super(factory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> get() {
		final List<Item> items = new ArrayList<>(11);
		final int[] data = ctx.players.local().appearance();
		for (final Slot slot : Slot.values()) {
			final int index = slot.getIndex();
			final Component c = ctx.widgets.widget(Constants.EQUIPMENT_WIDGET).component(slot.getComponentIndex()).component(1);
			final boolean v = c.visible();
			if (index < 0 || (index >= data.length || data[index] < 0) && !v) {
				continue;
			}
			items.add(new Item(ctx, c, v ? c.itemId() : data[index], v ? c.itemStackSize() : 1));
		}
		return items;
	}

	/**
	 * Returns the {@link org.powerbot.script.rt4.Item} at the spcified {@link Slot}.
	 *
	 * @param slot the {@link Slot} to get the {@link org.powerbot.script.rt4.Item} at
	 * @return the {@link org.powerbot.script.rt4.Item} in the provided slot
	 */
	public Item itemAt(final Slot slot) {
		final int index = slot.getIndex();
		final int[] data = ctx.players.local().appearance();
		if (index < 0) {
			return nil();
		}
		final Component c = ctx.widgets.widget(Constants.EQUIPMENT_WIDGET).component(slot.getComponentIndex()).component(1);
		final boolean v = c.visible();
		if ((index >= data.length || data[index] < 0) && !v) {
			return nil();
		}
		return new Item(ctx, c, v ? c.itemId() : data[index], v ? c.itemStackSize() : 1);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item nil() {
		return new Item(ctx, null, -1, -1);
	}

	/**
	 * An enumeration of equipment slots.
	 */
	public enum Slot {
		HEAD(0, 14),
		CAPE(1, 15),
		NECK(2, 16),
		MAIN_HAND(3, 17),
		TORSO(4, 18),
		OFF_HAND(5, 19),
		LEGS(7, 20),
		HANDS(9, 21),
		FEET(10, 22),
		RING(12, 23),
		QUIVER(13, 24);
		private final int index, component;

		Slot(final int index, final int component) {
			this.index = index;
			this.component = component;
		}

		public int getIndex() {
			return index;
		}

		public int getComponentIndex() {
			return component;
		}
	}
}
