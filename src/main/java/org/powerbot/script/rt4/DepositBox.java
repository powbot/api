package org.powerbot.script.rt4;

import org.powerbot.script.*;

import java.util.ArrayList;
import java.util.List;

/**
 * DepositBox
 * A utility class for depositing items, opening and closing a deposit box, and finding the closest usable bank deposit box.
 */
public class DepositBox extends ItemQuery<Item> {
	private static final int EMPTY_SLOT_ZOOM = 1777;

	public DepositBox(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> get() {
		final List<Item> items = new ArrayList<>();
		if (!opened()) {
			return items;
		}

		final Component[] a = ctx.widgets.component(Constants.DEPOSITBOX_WIDGET, Constants.DEPOSITBOX_ITEMS).components();
		for (final Component c : a) {
			if (c.valid() && c.modelZoom() != EMPTY_SLOT_ZOOM) {
				items.add(new Item(ctx, c));
			}
		}

		return items;
	}

	/**
	 * Whether or not the deposit box is currently opened.
	 *
	 * @return {@code true} if opened, {@code false} otherwise.
	 */
	public boolean opened() {
		return ctx.widgets.component(Constants.DEPOSITBOX_WIDGET, Constants.DEPOSITBOX_ITEMS).visible();
	}

	/**
	 * Attempts to close the deposit box using mouse.
	 *
	 * @return {@code true} if the deposit box was successfully closed, {@code false} otherwise.
	 */
	public boolean close() {
		return close(false);
	}

	/**
	 * Attempts to close the deposit box using either hotkeys or mouse.
	 *
	 * @param hotkey Whether to use hotkeys to close the interface or not.
	 * @return {@code true} if the deposit box was successfully closed, {@code false} otherwise.
	 */
	public boolean close(final boolean hotkey) {
		if (!opened()) {
			return true;
		}
		final boolean interacted;
		if (hotkey) {
			interacted = ctx.input.send("{VK_ESCAPE}");
		} else {
			interacted = ctx.widgets.component(Constants.DEPOSITBOX_WIDGET, 1).component(Constants.DEPOSITBOX_CLOSE).interact("Close");
		}
		return interacted && Condition.wait(()->!opened(), 30, 10);
	}

	/**
	 * Attempts to deposit the inventory into the deposit box.
	 *
	 * @return {@code true} if the inventory was successfully deposited, {@code false} otherwise.
	 */
	public boolean depositInventory() {
		return opened() && ctx.widgets.component(Constants.DEPOSITBOX_WIDGET, Constants.DEPOSITBOX_INVENTORY).interact("Deposit inventory");
	}

	/**
	 * Attempts to deposit the player's equipment into the deposit box.
	 *
	 * @return {@code true} if the player's equipment was successfully deposited, {@code false} otherwise.
	 */
	public boolean depositWornItems() {
		return opened() && ctx.widgets.component(Constants.DEPOSITBOX_WIDGET, Constants.DEPOSITBOX_WORN_ITEMS).interact("Deposit worn items");
	}

	/**
	 * Attempts to deposit loot into the deposit box.
	 *
	 * @return {@code true} if the loot has been successfully deposited, {@code false} otherwise.
	 */
	public boolean depositLoot() {
		return opened() && ctx.widgets.component(Constants.DEPOSITBOX_WIDGET, Constants.DEPOSITBOX_LOOT).interact("Deposit loot");
	}

	/**
	 * Attempts to deposit the specified item into the deposit box.
	 *
	 * @param id	 The Item ID.
	 * @param amount The amount to deposit.
	 * @return {@code true} if the item of the specified amount was successfully deposited, {@code false} otherwise.
	 */
	public boolean deposit(final int id, final Amount amount) {
		return deposit(id, amount.getValue());
	}

	/**
	 * Attempts to deposit the specified item into the deposit box.
	 *
	 * @param id	 The Item ID.
	 * @param amount The amount to deposit.
	 * @return {@code true} if the item of the specified amount was successfully deposited, {@code false} otherwise.
	 */
	public boolean deposit(final int id, final int amount) {
		final Item item = select().id(id).shuffle().poll();
		if (amount < 0 || !item.valid()) {
			return false;
		}
		final int count = select().id(id).count(true);
		final String action;
		if (count == 1 || amount == 1) {
			action = "Deposit-1";
		} else if (amount == 0 || count <= amount) {
			action = "Deposit-All";
		} else if (amount == 5 || amount == 10) {
			action = "Deposit-" + amount;
		} else {
			action = "Deposit-X";
		}
		if (!item.interact(action)) {
			return false;
		}
		if (action.endsWith("X")) {
			if (!Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					return ctx.chat.pendingInput();
				}
			}, 300, 10)) {
				return false;
			}
			Condition.sleep();
			ctx.input.sendln(String.valueOf(amount));
		}
		return Condition.wait(() -> count != select().id(id).count(true), 300, 10);
	}

	/**
	 * Deposits the players inventory excluding the specified ids.
	 *
	 * @param ids the ids of the items to ignore when depositing
	 * @return {@code true} if the items were deposited, determines if amount was matched; otherwise {@code false}
	 */
	public boolean depositAllExcept(final int... ids) {
		return depositAllExcept(item -> {
			final int id = item.id();
			for (final int i : ids) {
				if (id == i) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Deposits the players inventory excluding the specified item names.
	 *
	 * @param names the names of the items to ignore when depositing
	 * @return {@code true} if the items were deposited, determines if amount was matched; otherwise {@code false}
	 */
	public boolean depositAllExcept(final String... names) {
		return depositAllExcept(item -> {
			for (final String s : names) {
				if (s == null) {
					continue;
				}
				if (item.name().toLowerCase().contains(s.toLowerCase())) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Deposits the players inventory excluding the items that match the provided filter.
	 *
	 * @param filter the filter of the items to ignore when depositing
	 * @return {@code true} if the items were deposited, determines if amount was matched; otherwise {@code false}
	 */
	public boolean depositAllExcept(final Filter<Item> filter) {
		if (select().select(filter).count() == 0) {
			return depositInventory();
		}
		for (final Item i : select().shuffle()) {
			if (filter.accept(i)) {
				continue;
			}
			deposit(i.id(), Amount.ALL);
		}

		return select().count() == select(filter).count();
	}

	@Override
	public Item nil() {
		return ctx.inventory.nil();
	}

	public enum Amount {
		ONE(1), FIVE(5), TEN(10), ALL(0);

		private final int value;

		Amount(final int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
