package org.powerbot.script.rt4;

import org.powbot.stream.Streamable;
import org.powbot.stream.item.BankItemStream;
import org.powerbot.script.Random;
import org.powerbot.script.*;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import static org.powerbot.script.rt4.Constants.*;

/**
 * Bank
 * A utility class for withdrawing and depositing items, opening and closing the bank, and finding the closest usable bank.
 */
public class Bank extends ItemQuery<Item> implements Streamable<BankItemStream> {

	private static final Set<Tile> BANK_UNREACHABLES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(Constants.BANK_UNREACHABLES)));
	private static final Set<String> BANK_ACTIONS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("Bank", "Use", "Open", "bank", "use", "open")));

	public Bank(final ClientContext ctx) {
		super(ctx);
	}

	private static <T extends Locatable & Actionable> boolean UNREACHABLE_FILTER(final T entity) {
		if (BANK_UNREACHABLES.contains(entity.tile())) {
			return false;
		}

		for (final String action : entity.actions()) {
			if (BANK_ACTIONS.contains(action)) {
				return true;
			}
		}

		return false;
	}

	private Interactive getBank() {
		final Player p = ctx.players.local();
		final Tile t = p.tile();
		ctx.npcs.select().name(Constants.BANK_NPCS).viewable().select(Bank::UNREACHABLE_FILTER).nearest();
		ctx.objects.select().name(Constants.BANK_BOOTHS, Constants.BANK_CHESTS).viewable().select(Bank::UNREACHABLE_FILTER).nearest();
		if (!ctx.properties.getProperty("bank.antipattern", "").equals("disable")) {
			final Npc npc = ctx.npcs.poll();
			final GameObject object = ctx.objects.poll();
			return t.distanceTo(npc) < t.distanceTo(object) ? npc : object;
		}
		
		final double dist = Math.min(t.distanceTo(ctx.npcs.peek()), t.distanceTo(ctx.objects.peek()));
		final double d2 = Math.min(2d, Math.max(0d, dist - 1d));

		final List<Interactive> objectInteractives = new ArrayList<>();
		ctx.objects.within(dist + Random.nextInt(2, 5)).within(ctx.objects.peek(), d2);
		ctx.objects.addTo(objectInteractives);
		final int objLen = objectInteractives.size();
		if (objLen > 0) {
			return objectInteractives.get(Random.nextInt(0, objLen));
		}

		final List<Interactive> npcInteractives = new ArrayList<>();
		ctx.npcs.within(dist + Random.nextInt(2, 5)).within(ctx.npcs.peek(), d2);
		ctx.npcs.addTo(npcInteractives);
		final int npcLen = objectInteractives.size();
		return npcLen == 0 ? ctx.npcs.nil() : npcInteractives.get(Random.nextInt(0, npcLen));
	}

	/**
	 * Returns the absolute nearest bank for walking purposes. Do not use this to open the bank.
	 *
	 * @return the {@link Locatable} of the nearest bank or {@link Tile#NIL}
	 * @see #open()
	 */
	public Locatable nearest() {
		Locatable nearest = ctx.npcs.select().select(Bank::UNREACHABLE_FILTER).name(Constants.BANK_NPCS).nearest().poll();

		final Tile loc = ctx.players.local().tile();
		for (final GameObject object : ctx.objects.select().select(Bank::UNREACHABLE_FILTER).
				name(Constants.BANK_BOOTHS, Constants.BANK_CHESTS).nearest().limit(1)) {
			if (loc.distanceTo(object) < loc.distanceTo(nearest)) {
				nearest = object;
			}
		}
		if (nearest.tile() != Tile.NIL) {
			return nearest;
		}
		return Tile.NIL;
	}

	/**
	 * Determines if a bank is present in the loaded region.
	 *
	 * @return {@code true} if a bank is present; otherwise {@code false}
	 */
	public boolean present() {
		return nearest() != Tile.NIL;
	}

	/**
	 * @return {@code true} if any bank is in viewport; otherwise {@code false}
	 */
	public boolean inViewport() {
		return getBank().valid();
	}

	/**
	 * Opens a random in-view bank.
	 * Do not continue execution within the current poll after this method so BankPin may activate.
	 *
	 * @return {@code true} if the bank was opened; otherwise {@code false}
	 */
	public boolean open() {
		if (opened()) {
			return true;
		}

		final Interactive interactive = getBank();
		if (!interactive.valid()) {
			return false;
		}

		final Filter<MenuCommand> filter = command -> BANK_ACTIONS.contains(command.action);

		if (interactive.hover()) {
			Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					return ctx.menu.indexOf(filter) != -1;
				}
			}, 100, 3);
		}

		if (interactive.interact(filter)) {
			do {
				Condition.wait(new Condition.Check() {
					@Override
					public boolean poll() {
						return opened();
					}
				}, 150, 15);
			} while (ctx.players.local().inMotion());

			Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					return opened();
				}
			}, 100, 15);
		}
		return opened();

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
		for (final Component c : ctx.widgets.widget(Constants.BANK_WIDGET).component(Constants.BANK_ITEMS).components()) {
			final int id = c.itemId(), stack = c.itemStackSize();
			if (id >= 0 && stack > 0) {
				items.add(new Item(ctx, c, id, stack));
			}
		}
		return items;
	}

	@Override
	public Item nil() {
		return new Item(ctx, null, -1, -1, -1);
	}

	/**
	 * @return {@code true} if the bank is opened; otherwise {@code false}
	 */
	public boolean opened() {
		return ctx.widgets.widget(Constants.BANK_WIDGET).component(Constants.BANK_MASTER).visible();
	}

	/**
	 * Closes the bank
	 * @param key if {@code true}, and escape closing enabled, will press escape key. If {@code false}, clicks the close button.
	 * @return {@code true} if the bank is already closed or is successfully closed; {@code false} otherwise.
	 */
	public boolean close(final boolean key) {
		if (!opened()) {
			return true;
		}
		final Component closeButton = ctx.widgets.component(BANK_WIDGET, BANK_MASTER, BANK_CLOSE);
		return ((key && ctx.game.settings.escClosingEnabled() && ctx.input.send("{VK_ESCAPE}")) || closeButton.click())
				&& Condition.wait(() -> !opened(), 30, 10);
	}

	/**
	 * Closes the bank.
	 *
	 * @return {@code true} if the bank was already or has been closed, {@code false} otherwise.
	 */
	public boolean close() {
		return close(false);
	}

	/**
	 * Withdraws an item with the provided id and amount.
	 *
	 * @param id     the id of the item
	 * @param amount the amount to withdraw
	 * @return {@code true} if the item was withdrawn, does not determine if amount was matched; otherwise, {@code false}
	 */
	public boolean withdraw(final int id, final Amount amount) {
		return withdraw(id, amount.getValue());
	}

	/**
	 * Withdraws an item with the provided name and amount.
	 *
	 * @param id      the id of the item
	 * @param amount  the amount to withdraw
	 * @param waitFor the condition to wait for
	 * @return the return value of {@code waitFor} if items were attempted to be withdrawn. False otherwise.
	 */
	public boolean withdraw(final int id, final Amount amount, final Callable<Boolean> waitFor) {
		return withdraw(id, amount.getValue(), waitFor);
	}

	/**
	 * Withdraws an item with the provided name and amount.
	 * If multiple items of the same name are present, the first one is chosen.
	 *
	 * @param name   the name of the item
	 * @param amount the amount to withdraw
	 * @return {@code true} if the item was withdrawn, does not determine if amount was matched; otherwise, {@code false}
	 */
	public boolean withdraw(final String name, final Amount amount) {
		return withdraw(select().name(name).poll(), amount.getValue());
	}

	/**
	 * Withdraws an item with the provided name and amount.
	 * If multiple items of the same name are present, the first one is chosen.
	 *
	 * @param name   the name of the item
	 * @param amount the amount to withdraw
	 * @return {@code true} if the item was withdrawn, does not determine if amount was matched; otherwise, {@code false}
	 */
	public boolean withdraw(final String name, final Amount amount, final Callable<Boolean> waitFor) {
		return withdraw(select().name(name).poll(), amount.getValue(), waitFor);
	}

	/**
	 * Withdraws an item with the provided name and amount.
	 * If multiple items match the filter, the first one is chosen.
	 *
	 * @param filter the filter to apply to the items in the bank
	 * @param amount the amount to withdraw
	 * @return {@code true} if the item was withdrawn, does not determine if amount was matched; otherwise, {@code false}
	 */
	public boolean withdraw(final Filter<Item> filter, final Amount amount) {
		return withdraw(select().select(filter).poll(), amount.getValue());
	}

	/**
	 * Withdraws an item with the provided name and amount.
	 * If multiple items match the filter, the first one is chosen.
	 *
	 * @param filter the filter to apply to the items in the bank
	 * @param amount the amount to withdraw
	 * @return {@code true} if the item was withdrawn, does not determine if amount was matched; otherwise, {@code false}
	 */
	public boolean withdraw(final Filter<Item> filter, final Amount amount, final Callable<Boolean> waitFor) {
		return withdraw(select().select(filter).poll(), amount.getValue(), waitFor);
	}

	/**
	 * Withdraws an item with the provided id and amount.
	 *
	 * @param id     the id of the item
	 * @param amount the amount to withdraw
	 * @return {@code true} if the item was withdrawn, does not determine if amount was matched; otherwise, {@code false}
	 */
	public boolean withdraw(final int id, final int amount) {
		return withdraw(select().id(id).poll(), amount);
	}

	/**
	 * Withdraws an item with the provided name and amount.
	 *
	 * @param id      the id of the item
	 * @param amount  the amount to withdraw
	 * @param waitFor the condition to wait for
	 * @return the return value of {@code waitFor} if items were attempted to be withdrawn. False otherwise.
	 */
	public boolean withdraw(final int id, final int amount, final Callable<Boolean> waitFor) {
		return withdraw(select().id(id).poll(), amount, waitFor);
	}

	/**
	 * Withdraws an item with the provided name and amount.
	 * If multiple items of the same name are present, the first one is chosen.
	 *
	 * @param name   the name of the item
	 * @param amount the amount to withdraw
	 * @return {@code true} if the item was withdrawn, does not determine if amount was matched; otherwise, {@code false}
	 */
	public boolean withdraw(final String name, final int amount) {
		return withdraw(select().name(name).poll(), amount);
	}

	/**
	 * Withdraws an item with the provided name and amount.
	 * If multiple items of the same name are present, the first one is chosen.
	 *
	 * @param name    the filter to apply to the items in the bank
	 * @param amount  the amount to withdraw
	 * @param waitFor the condition to wait for
	 * @return the return value of {@code waitFor} if items were attempted to be withdrawn. False otherwise.
	 */
	public boolean withdraw(final String name, final int amount, final Callable<Boolean> waitFor) {
		return withdraw(select().name(name).poll(), amount, waitFor);
	}

	/**
	 * Withdraws an item with the provided name and amount.
	 * If multiple items match the filter, the first one is chosen.
	 *
	 * @param filter the filter to apply to the items in the bank
	 * @param amount the amount to withdraw
	 * @return {@code true} if the item was withdrawn, does not determine if amount was matched; otherwise, {@code false}
	 */
	public boolean withdraw(final Filter<Item> filter, final int amount) {
		return withdraw(select().select(filter).poll(), amount);
	}

	/**
	 * Withdraws an item with the provided name and amount.
	 * If multiple items match the filter, the first one is chosen.
	 *
	 * @param filter  the filter to apply to the items in the bank
	 * @param amount  the amount to withdraw
	 * @param waitFor the condition to wait for
	 * @return the return value of {@code waitFor} if items were attempted to be withdrawn. False otherwise.
	 */
	public boolean withdraw(final Filter<Item> filter, final int amount, final Callable<Boolean> waitFor) {
		return withdraw(select().select(filter).poll(), amount, waitFor);
	}

	/**
	 * Withdraws an item with the provided item and amount.
	 *
	 * @param item   the item instance
	 * @param amount the amount to withdraw
	 * @return {@code true} if the item was withdrawn, does not determine if amount was matched; otherwise, {@code false}
	 */
	public boolean withdraw(final Item item, final int amount) {
		return withdrawAmount(item, amount) > 0;
	}

	/**
	 * Withdraws the specified amount of the specified item and waits for the specified condition to be true.
	 *
	 * @param item    the item to withdraw
	 * @param amount  the amount to withdraw
	 * @param waitFor the condition to wait for
	 * @return the return value of {@code waitFor} if items were attempted to be withdrawn. False otherwise.
	 */
	public boolean withdraw(final Item item, final int amount, final Callable<Boolean> waitFor) {
		if (!opened() || !item.valid() || amount < Amount.values()[0].getValue() || !scrollToItem(item)) {
			return false;
		} else if (!item.component().visible()) {
			currentTab(0);
		}
		final int bankCount = select().id(item.id()).count(true);

		final String action = amount == Amount.PLACEHOLDER.getValue()
			? "Placeholder"
			: bankActionString("Withdraw", item, bankCount, amount);
		if ((item.contains(ctx.input.getLocation()) && ctx.menu.click(c -> c.action.equalsIgnoreCase(action)))
			|| item.interact(c -> c.action.equalsIgnoreCase(action))) {
			if (action.endsWith("X")) {
				if (!Condition.wait(ctx.chat::pendingInput)) {
					return false;
				}
				Condition.sleep();
				ctx.input.sendln(String.valueOf(amount));
			}
			return waitFor == null || Condition.wait(waitFor);
		}
		return false;
	}

	/**
	 * Withdraws the specified amount of the specified item.
	 *
	 * @param filter the filter to apply to the items.
	 *               If multiple items matching the filter exist, only the first is withdrawn
	 * @param amount the amount to withdraw
	 * @return the amount successfully withdrawn
	 */
	public int withdrawAmount(final Filter<Item> filter, final Amount amount) {
		return withdrawAmount(filter, amount.getValue());
	}

	/**
	 * Withdraws the specified amount of the specified item.
	 *
	 * @param filter the filter to apply to the items.
	 *               If multiple items matching the filter exist, only the first is withdrawn
	 * @param amount the amount to withdraw
	 * @return the amount successfully withdrawn
	 */
	public int withdrawAmount(final Filter<Item> filter, final int amount) {
		return withdrawAmount(select().select(filter).poll(), amount);
	}

	/**
	 * Withdraws the specified amount of the specified item.
	 *
	 * @param name   the name of the item to withdraw.
	 *               If multiple items with the same name exist, only the first is withdrawn
	 * @param amount the amount to withdraw
	 * @return the amount successfully withdrawn
	 */
	public int withdrawAmount(final String name, final Amount amount) {
		return withdrawAmount(name, amount.getValue());
	}

	/**
	 * Withdraws the specified amount of the specified item.
	 *
	 * @param name   the name of the item to withdraw.
	 *               If multiple items with the same name exist, only the first is withdrawn
	 * @param amount the amount to withdraw
	 * @return the amount successfully withdrawn
	 */
	public int withdrawAmount(final String name, final int amount) {
		return withdrawAmount(select().name(name).poll(), amount);
	}

	/**
	 * Withdraws the specified amount of the specified item.
	 *
	 * @param id     the id of the item to withdraw
	 * @param amount the amount to withdraw
	 * @return the amount successfully withdrawn
	 */
	public int withdrawAmount(final int id, final Amount amount) {
		return withdrawAmount(id, amount.getValue());
	}

	/**
	 * Withdraws the specified amount of the specified item.
	 *
	 * @param id     the id of the item to withdraw
	 * @param amount the amount to withdraw
	 * @return the amount successfully withdrawn
	 */
	public int withdrawAmount(final int id, final int amount) {
		return withdrawAmount(select().id(id).poll(), amount);
	}

	/**
	 * Withdraws the specified amount of the specified item.
	 *
	 * @param item   the item to withdraw
	 * @param amount the amount to withdraw
	 * @return the amount successfully withdrawn
	 */
	public int withdrawAmount(final Item item, final int amount) {
		final int inventoryCount = ctx.inventory.select().count(true);
		final AtomicInteger count = new AtomicInteger(0);
		withdraw(item, amount, () -> {
			count.set(ctx.inventory.select().count(true) - inventoryCount);
			return count.get() > 0;
		});
		return count.get();
	}

	private boolean scrollToItem(final Item item) {
		return ctx.widgets.scroll(
				item.component,
				ctx.widgets.component(BANK_WIDGET, BANK_ITEMS),
				ctx.widgets.component(BANK_WIDGET, BANK_SCROLLBAR),
				true
		);
	}

	private String bankActionString(String action, final Item item, final int count, final int amount) {
		action = action + "-";
		if (amount == 1 || amount == 5 || amount == 10) {
			action += amount;
		} else if (amount == Amount.ALL_BUT_ONE.getValue() && count > 1) {
			action += "All-but-1";
		} else if (amount == Amount.X.getValue()) {
			action += withdrawXAmount();
		} else if (amount == Amount.ALL.getValue() || count <= amount) {
			action += "All";
		} else if (itemContainsAmountAction(item, amount)) {
			action += amount;
		} else {
			action += "X";
		}
		return action;
	}

	private boolean itemContainsAmountAction(final Item item, final int amount) {
		if (item.hover()) {
			if (Condition.wait(() -> ctx.menu.containsAction("Withdraw") || ctx.menu.containsAction("Deposit"), 20, 10)) {
				final String s = "-".concat(String.valueOf(amount));
				return ctx.menu.contains(c -> c.action.endsWith(s));
			}
		}
		return false;
	}

	/**
	 * Deposits an item with the provided id and amount.
	 *
	 * @param id     the id of the item
	 * @param amount the amount to deposit
	 * @return {@code true} if the item was deposited, does not determine if amount was matched; otherwise {@code false}
	 */
	public boolean deposit(final int id, final Amount amount) {
		return deposit(id, amount.getValue());
	}

	/**
	 * Deposits an item with the provided id and amount.
	 *
	 * @param id     the id of the item
	 * @param amount the amount to deposit
	 * @return {@code true} if the item was deposited, does not determine if amount was matched; otherwise {@code false}
	 */
	public boolean deposit(final int id, final int amount) {
		if (!opened() || amount < 0) {
			return false;
		}
		final Item item = ctx.inventory.select().id(id).shuffle().poll();
		if (!item.valid()) {
			return false;
		}
		final int inventoryCount = ctx.inventory.select().id(id).count(true);
		final String action = bankActionString("Deposit", item, inventoryCount, amount);
		if ((item.contains(ctx.input.getLocation())
				&& ctx.menu.click(c -> c.action.equalsIgnoreCase(action)))
				|| item.interact(c -> c.action.equalsIgnoreCase(action))) {
			if (action.endsWith("X")) {
				if (!Condition.wait(ctx.chat::pendingInput)) {
					return false;
				}
				Condition.sleep();
				ctx.input.sendln(String.valueOf(amount));
			}
		}
		return Condition.wait(() -> ctx.inventory.select().id(item.id()).count(true) != inventoryCount);
	}

	/**
	 * Deposits an item with the provided name and amount.
	 *
	 * @param name   the name of the item
	 * @param amount the amount to deposit
	 * @return {@code true} if the item was deposited, does not determine if amount was matched; otherwise {@code false}
	 */
	public boolean deposit(final String name, final Amount amount) {
		return deposit(name, amount.getValue());
	}

	/**
	 * Deposits an item with the provided name and amount.
	 *
	 * @param name   the name of the item
	 * @param amount the amount to deposit
	 * @return {@code true} if the item was deposited, does not determine if amount was matched; otherwise {@code false}
	 */
	public boolean deposit(final String name, final int amount) {
		return deposit(ctx.inventory.select().name(name).peek().id(), amount);
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
				if (item.name().equalsIgnoreCase(s)) {
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
		if (ctx.inventory.select().select(filter).count() == 0) {
			return depositInventory();
		}
		for (final Item i : ctx.inventory.select().shuffle()) {
			if (filter.accept(i)) {
				continue;
			}
			deposit(i.id(), Amount.ALL);
		}

		return ctx.inventory.select().count() == ctx.inventory.select(filter).count();
	}

	/**
	 * @return {@code true} if bank has tabs; otherwise {@code false}
	 */

	public boolean tabbed() {
		return ctx.varpbits.varpbit(Constants.BANK_TABS) != Constants.BANK_TABS_HIDDEN;
	}

	/**
	 * @return the index of the current bank tab
	 */
	public int currentTab() {
		return ctx.varpbits.varpbit(Constants.BANK_STATE) / 4;
	}

	/**
	 * Changes the current tab to the provided index.
	 *
	 * @param index the index desired
	 * @return {@code true} if the tab was successfully changed; otherwise {@code false}
	 */
	public boolean currentTab(final int index) {
		final Component c = ctx.widgets.component(Constants.BANK_WIDGET, 21).component(index);
		return (currentTab() == index) || c.click() && Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return currentTab() == index;
			}
		}, 100, 8);
	}

	/**
	 * Returns the item in the specified tab if it exists.
	 *
	 * @param index the tab index
	 * @return the {@link Item} displayed in the tab; otherwise {@link #nil()}
	 */
	public Item tabItem(final int index) {
		final Component c = ctx.widgets.component(Constants.BANK_WIDGET, 11).component(10 + index);
		if (c != null && c.valid() && c.itemId() != -1) {
			return new Item(ctx, c);
		}

		return nil();
	}

	/**
	 * @return {@code true} if noted withdrawing mode is selected; otherwise {@code false}
	 */
	public boolean withdrawModeNoted() {
		return ctx.varpbits.varpbit(Constants.BANK_STATE, 0, 0x1) == 1;
	}

	/**
	 * Returns the currently selected withdraw mode.
	 *
	 * @return {@link Amount#PLACEHOLDER} if no amount is specified. If not, it returns the respective selected withdraw mode quantity.
	 */
	public Amount withdrawModeQuantity() {
		final Amount[] amounts = {Amount.ONE, Amount.FIVE, Amount.TEN, Amount.X, Amount.ALL};
		final int withdrawModeNumber = ctx.varpbits.varpbit(Constants.BANK_QUANTITY, 2, 0x7);
		if (withdrawModeNumber >= 0 && withdrawModeNumber < amounts.length) {
			return amounts[withdrawModeNumber];
		}
		return Amount.PLACEHOLDER;
	}

	/**
	 * Gives the component value of the quantity amount to be used.
	 *
	 * @param amount specifies the amount to get the component for.
	 * @return {@code -1} if the amount specified doesn't exist. If not, it returns the respective component value.
	 */
	public int quantityComponentValue(final Amount amount) {
		final int quantityComponentValue;
		switch (amount) {
			case ONE:
				quantityComponentValue = Constants.BANK_QUANTITY_ONE;
				break;
			case FIVE:
				quantityComponentValue = Constants.BANK_QUANTITY_FIVE;
				break;
			case TEN:
				quantityComponentValue = Constants.BANK_QUANTITY_TEN;
				break;
			case X:
				quantityComponentValue = Constants.BANK_QUANTITY_X;
				break;
			case ALL:
				quantityComponentValue = Constants.BANK_QUANTITY_ALL;
				break;
			default:
				quantityComponentValue = -1;
		}
		return quantityComponentValue;
	}

	/**
	 * Select or verify the current withdraw quantity mode within the bank. Bank must be opened if you intend to set, but can be checked without opening.
	 *
	 * @param amount the relevant amount enum
	 * @return {@code true} if the passed amount was set, or has been set.
	 */
	public boolean withdrawModeQuantity(final Amount amount) {
		final int quantityComponentValue;
		if (withdrawModeQuantity() == amount) {
			return true;
		} else if (!opened() || (quantityComponentValue = quantityComponentValue(amount)) < -1) {
			return false;
		} else {
			return (ctx.widgets.widget(Constants.BANK_WIDGET).component(quantityComponentValue).click() && Condition.wait(()-> withdrawModeQuantity() == amount, 30, 10));
		}
	}

	/**
	 * Check the current amount that is set to Withdraw-X
	 *
	 * @return The amount representation of withdraw-x
	 */
	public int withdrawXAmount() {
		return ctx.varpbits.varpbit(Constants.BANK_X_VALUE) / 2;
	}

	/**
	 * @param noted {@code true} to set withdrawing mode to noted, {@code false} to set it to withdraw normally
	 * @return {@code true} if withdrawing mode is already set, or was successfully set to the desired withdrawing mode; otherwise {@code false}
	 */
	public boolean withdrawModeNoted(final boolean noted) {
		return withdrawModeNoted() == noted || (ctx.widgets.widget(Constants.BANK_WIDGET).component(noted ? Constants.BANK_NOTE : Constants.BANK_ITEM).interact(noted ? "Note" : "Item") && Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return withdrawModeNoted() == noted;
			}
		}, 30, 10));
	}

	/**
	 * @return {@code true} if deposit inventory button was clicked successfully; otherwise {@code false}
	 */
	public boolean depositInventory() {
		return ctx.inventory.get().isEmpty() || ctx.widgets.widget(Constants.BANK_WIDGET).component(Constants.BANK_DEPOSIT_INVENTORY).interact("Deposit");
	}

	/**
	 * @return {@code true} if deposit equipment button was clicked successfully; otherwise {@code false}
	 */
	public boolean depositEquipment() {
		return ctx.widgets.widget(Constants.BANK_WIDGET).component(Constants.BANK_DEPOSIT_EQUIPMENT).interact("Deposit");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BankItemStream toStream() {
		return new BankItemStream(ctx, get().stream());
	}

	/**
	 * Amount
	 * An enumeration providing standard bank amount options.
	 * X is the relative to whatever the current value of X is.
	 */
	public enum Amount {
		X, PLACEHOLDER, ALL_BUT_ONE, ALL, ONE, FIVE(5), TEN(10);

		private final int value;

		Amount() {
			value = ordinal() - 3;
		}

		Amount(final int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
