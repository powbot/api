package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.*;

import java.awt.Component;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Menu
 * <p>
 * An interface of the menu which appears when right-clicking in the game. An example
 * f this menu is:
 * <ul>
 *     <li><b>Walk here</b></li>
 *     <li><b>Examine</b> Tree</li>
 *     <li><b>Drop</b> Coins</li>
 * </ul>
 */
public class Menu extends ClientAccessor {

	public Menu(final ClientContext ctx) {
		super(ctx);
	}

	public static Filter<? super MenuCommand> filter(final String action) {
		return filter(action, null);
	}

	public static Filter<? super MenuCommand> filter(final String action, final String option) {
		final String a = action != null ? action.toLowerCase() : null;
		final String o = option != null ? option.toLowerCase() : null;
		return (Filter<MenuCommand>) command -> (a == null || command.action.toLowerCase().contains(a)) &&
			(o == null || command.option.toLowerCase().contains(o));
	}

	/**
	 * The dimensions of the menu bounds.
	 *
	 * @return A rectangle representing the dimensions of the menu.
	 */
	public Rectangle bounds() {
		final Client client = ctx.client();
		if (client == null || !opened()) {
			return new Rectangle(-1, -1, -1, -1);
		}
		return new Rectangle(client.getMenuX(), client.getMenuY(), client.getMenuWidth(), client.getMenuHeight());
	}

	/**
	 * Whether or not the menu is opened.
	 *
	 * @return {@code true} if the mennu is opened, {@code false} otherwise.
	 */
	public boolean opened() {
		final Client client = ctx.client();
		return client != null && client.isMenuOpen();
	}

	/**
	 * Provides the index of the menu command given the specified filter.
	 *
	 * @param filter The filter to apply to the menu.
	 * @return The index of the menu command, or {@code -1} if it was not found.
	 */
	public int indexOf(final Filter<? super MenuCommand> filter) {
		final java.util.List<String> actions = getMenuActions(), options = getMenuOptions();
		for (int i = 0; i < ctx.client().getMenuCount(); i++) {
			if (filter.accept(new MenuCommand(actions.size() > i ? actions.get(i) : null, options.size() > i ? options.get(i) : null))) {
				return i;
			}
		}
		return -1;
	}

	public java.util.List<String> getMenuActions() {
		final String[] menuActions = ctx.client().getMenuActions();
		return stripMenuValues(menuActions);
	}

	public java.util.List<String> getMenuOptions() {
		final String[] menuOptions = ctx.client().getMenuOptions();
		return stripMenuValues(menuOptions);
	}

	private java.util.List<String> stripMenuValues(final String[] values) {
		final java.util.List<String> list = new ArrayList<>();
		if (values != null) {
			for (int i = ctx.client().getMenuCount() - 1; i >= 0; i--) {
				if (values[i] == null) continue;
				list.add(StringUtils.stripHtml(values[i]));
			}
		}
		return list;
	}

	/**
	 * Checks if the menu contains any {@link MenuCommand} matching the filter.
	 *
	 * @param filter the filter to apply
	 * @return {@code true} if a {@link MenuCommand} exists, {@code false} otherwise
	 */
	public boolean contains(final Filter<? super MenuCommand> filter) {
		return indexOf(filter) != -1;
	}

	/**
	 * Checks if the menu contains the specified action.
	 *
	 * @param action The action string to search for. This does a case insensitive contains.
	 * @return {@code true} if the action exists, {@code false} otherwise.
	 */
	public boolean containsAction(final String action) {
		return indexOf(c -> c.action.toLowerCase().contains(action.toLowerCase())) != -1;
	}

	/**
	 * Attempts to hover over the menu command, given the provided filter.
	 *
	 * @param filter The filter to apply to the menu.
	 * @return {@code true} if the mouse is within the bounds of the specified {@link MenuCommand},
	 * {@code false} otherwise.
	 */
	public boolean hover(final Filter<? super MenuCommand> filter) {
		return click(filter, false);
	}

	/**
	 * Attempts to click the menu command provided by the filter.
	 *
	 * @param filter The filter to apply to the menu.
	 * @return {@code true} if the mouse has successfully clicked within the bounds of the {@link MenuCommand}.
	 */
	public boolean click(final Filter<? super MenuCommand> filter) {
		return click(filter, true);
	}

	/**
	 * Attempts to click the menu command provided by the filter.
	 *
	 * @param filter The filter to apply to the menu.
	 * @param click  Whether or not to left-click.
	 * @return {@code true} if the mouse has successfully clicked within the bounds of the {@link MenuCommand}
	 */
	private boolean click(final Filter<? super MenuCommand> filter, final boolean click) {
		final Client client = ctx.client();
		int slot;
		if (client == null || (slot = indexOf(filter)) == -1) {
			return false;
		}

		if (click && !client.isMenuOpen() && slot == 0) {
			return ctx.input.click(true);
		}

		if (!client.isMenuOpen()) {
			if (!ctx.input.click(false)) {
				return false;
			}
		}
		if (!Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return client.isMenuOpen();
			}
		}, 15, 10) || (slot = indexOf(filter)) == -1) {
			return false;
		}
		final Rectangle rectangle = new Rectangle(client.getMenuX(), client.getMenuY() + 19 + slot * 15, client.getMenuWidth(), 15);
		Condition.sleep(Random.hicks(slot));
		if (!ctx.input.move(
			Random.nextInt(rectangle.x, rectangle.x + rectangle.width),
			Random.nextInt(rectangle.y, rectangle.y + rectangle.height)) || !client.isMenuOpen()) {
			return false;
		}
		final Point p = ctx.input.getLocation();
		return client.isMenuOpen() && rectangle.contains(p) && (!click || ctx.input.click(true));
	}

	/**
	 * Attempts to close the menu.
	 *
	 * @return {@code true} if the menu was closed, {@code false} otherwise.
	 */
	public boolean close() {
		final Client client = ctx.client();
		if (client == null) {
			return false;
		}
		if (!client.isMenuOpen()) {
			return true;
		}

		final Component c = ctx.input.getComponent();
		final Dimension d = new Dimension(c != null ? c.getWidth() : 0, c != null ? c.getHeight() : 0);
		final int mx = client.getMenuX(), my = client.getMenuY();
		final int w = (int) d.getWidth(), h = (int) d.getHeight();
		int x1, x2;
		final int y1, y2;
		x1 = x2 = mx;
		y1 = y2 = Math.min(h - 5, Math.max(4, my + Random.nextInt(-10, 10)));
		x1 = Math.max(4, x1 + Random.nextInt(-30, -10));
		x2 = x2 + client.getMenuWidth() + Random.nextInt(10, 30);
		if (x2 <= w - 5 && (x1 - mx >= 5 || Random.nextBoolean())) {
			ctx.input.move(x2, y2);
		} else {
			ctx.input.move(x1, y1);
		}
		return Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return client.isMenuOpen();
			}
		}, 10, 50);
	}

	/**
	 * Returns an array of all the current menu items ([action_1 option_1, action_2 option_2, ...]).
	 *
	 * @return the array of menu items
	 */
	public String[] items() {
		final MenuCommand[] m = commands();
		final String[] arr = new String[ctx.client().getMenuCount()];
		for (int i = 0; i < ctx.client().getMenuCount(); i++) {
			arr[i] = (m[i].action + " " + m[i].option).trim();
		}
		return arr;
	}

	/**
	 * Returns an array of the current menu commands available.
	 *
	 * @return A {@link MenuCommand} array.
	 */
	public MenuCommand[] commands() {
		final java.util.List<String> actions = getMenuActions(), options = getMenuOptions();
		final MenuCommand[] arr = new MenuCommand[ctx.client().getMenuCount()];
		for (int i = 0; i < ctx.client().getMenuCount(); i++) {
			arr[i] = new MenuCommand(actions.size() > i ? actions.get(i) : null, options.size() > i ? options.get(i) : null);
		}
		return arr;
	}

	@Deprecated
	public static class Command extends MenuCommand {
		protected Command(final String a, final String o) {
			super(a, o);
		}
	}
}
