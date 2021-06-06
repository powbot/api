package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.extended.IMobileClient;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;

import java.applet.Applet;
import java.awt.*;
import java.util.function.Function;

import static org.powerbot.script.rt4.Constants.*;

/**
 * Game
 * A utility class used for interacting with game tabs, retrieving miscellaneous game values, and converting points to the viewport.
 */
public class Game extends ClientAccessor {
	public static final int[] ARRAY_SIN = new int[16384];
	public static final int[] ARRAY_COS = new int[16384];

	public static final int FIXED_VIEWPORT_START_X = 4;
	public static final int FIXED_VIEWPORT_START_Y = 4;
	public static final int FIXED_VIEWPORT_END_X = 515;
	public static final int FIXED_VIEWPORT_END_Y = 338;

	public Settings settings;

	static {
		for (int i = 0; i < 16384; i++) {
			ARRAY_SIN[i] = (int) (65536d * Math.sin(i * 0.0030679615d));
			ARRAY_COS[i] = (int) (65536d * Math.cos(i * 0.0030679615d));
		}
	}

	public Game(final ClientContext ctx) {
		super(ctx);
		this.settings = new Settings();
	}

	/**
	 * Logs out of the game into either the lobby or login screen.
	 *
	 * @return {@code true} if successfully logged out; otherwise {@code false}
	 */
	public boolean logout() {
		if (ctx.game.tab(Tab.LOGOUT)) {
			final Component logoutButton = ctx.widgets.component(LOGOUT_BUTTON_WIDGET, LOGOUT_BUTTON_COMPONENT);
			if (logoutButton.valid() && logoutButton.visible()) {
				return logoutButton.click("Logout") && Condition.wait(() -> clientState() == GAME_LOGIN);
			}
			ctx.components.select(true, WORLD_SELECTOR_WIDGET).texture(LOGOUT_DOOR_TEXTURE);
			if (!ctx.components.isEmpty()) {
				final Component door = ctx.components.poll();
				return door.click("Logout") && Condition.wait(() -> clientState() == GAME_LOGIN);
			}
		}
		return false;
	}

	/**
	 * Attempts to open the specified game tab on the user interface by clicking. If the
	 * tab is already opened, it will return {@code true}.
	 *
	 * @param tab The tab to switch to
	 * @return {@code true} if the tab is open, {@code false} otherwise.
	 */
	public boolean tab(final Tab tab) {
		return tab(tab, false);
	}

	/**
	 * Attempts to open the specified game tab on the user interface. If the
	 * tab is already opened, it will return {@code true}.
	 *
	 * @param tab    The tab to switch to
	 * @param hotkey whether or not to use hotkeys to open the tab
	 * @return {@code true} if the tab is open, {@code false} otherwise.
	 */
	public boolean tab(final Tab tab, final boolean hotkey) {
		if (tab == tab()) {
			return true;
		}
		final Keybind keybind;
		final boolean interacted;
		final String key;
		if (hotkey && (keybind = Keybind.keybind(tab)) != Keybind.NONE && !(key = keybind.key(ctx)).equals("")) {
			interacted = ctx.input.send(key);
		} else {
			final Component c = getByTexture(tab.textures);
			interacted = c != null && ((ctx.client().isMobile() && c.click(true)) || c.click(command -> {
				for (final String tip : tab.tips) {
					if (command.action.equals(tip)) {
						return true;
					}
				}
				return false;
			}));
		}
		return interacted && Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return tab() == tab;
			}
		}, 50, 10);
	}

	/**
	 * Returns the current tab which is opened.
	 *
	 * @return The open game tab.
	 */
	public Tab tab() {
		for (final Tab tab : Tab.values()) {
			final Component c = getByTexture(tab.textures);
			if (c == null) {
				continue;
			}
			try {
				final Component c2 = ctx.widgets.widget(c.widget().id()).component(c.index() - openedTabIndexOffset(tab));
				if (c2.textureId() != -1) {
					return tab;
				}
			} catch (final ArrayIndexOutOfBoundsException ignored) {
			}
		}
		return Tab.NONE;
	}

	private int openedTabIndexOffset(final Tab tab) {
		if (resizable() && bottomLineTabs()) {
			switch (tab) {
				case LOGOUT:
					return 1;
				case ACCOUNT_MANAGEMENT:
				case FRIENDS_LIST:
				case IGNORED_LIST:
				case CLAN_CHAT:
				case SETTINGS:
				case EMOTES:
				case MUSIC:
					return 6;
				default:
					return 7;
			}
		}

		return 7;
	}

	private Component getByTexture(final int... textures) {
		Widget w = new Widget(ctx, 0);
		if (ctx.client().isMobile()) {
			w = ctx.widgets.widget(601);
		} else if (resizable()) {
			w = ctx.widgets.widget(bottomLineTabs() ? 164 : 161);
		} else {
			w = ctx.widgets.widget(548);
		}

		for (final Component c : w.components()) {
			for (final int t : textures) {
				if (c.textureId() == t) {
					return c;
				}
			}
		}
		return null;
	}

	/**
	 * Whether or not interfaces can be closed with ESC button
	 *
	 * @return {@code true} if interfaces can be closed with ESC, {@code false} otherwise.
	 */
	public boolean escapeClosing() {
		return ctx.varpbits.varpbit(1224) < 0;
	}

	/**
	 * Whether or not the player is currently logged in.
	 *
	 * @return {@code true} if logged in, {@code false} otherwise.
	 */
	public boolean loggedIn() {
		final int c = clientState();
		return c == Constants.GAME_LOADED || c == Constants.GAME_LOADING;
	}

	/**
	 * The dimensions of the Applet.
	 *
	 * @return The dimensions of the applet.
	 */
	public Dimension dimensions() {
		final Rectangle bounds = ctx.input.inputBounds();
		if (bounds != null) {
			return new Dimension(bounds.width, bounds.height);
		}
		return new Dimension(-1, -1);
	}

	/**
	 * The current client state.
	 *
	 * @return The current client state.
	 * @see Constants
	 */
	public int clientState() {
		final IClient client = ctx.client();
		return client != null ? client.getClientState() : -1;
	}

	/**
	 * @return The client's login state
	 */
	public int loginState() {
		final IClient c = ctx.client();
		return c != null ? c.getLoginState() : -1;
	}

	/**
	 * The current floor the client is at.
	 *
	 * @return the current floor the player is at, or -1 if the client has yet to be instantiated.
	 */
	public int floor() {
		final IClient client = ctx.client();
		return client != null ? client.getFloor() : -1;
	}

	/**
	 * Determines the current {@link Crosshair} displayed.
	 *
	 * @return the displayed {@link Crosshair}
	 */
	public Crosshair crosshair() {
		final IClient client = ctx.client();
		final int type = client != null ? client.getCrosshairIndex() : -1;
		if (type < 0 || type > 2) {
			return Crosshair.NONE;
		}
		return Crosshair.values()[type];
	}

	/**
	 * The relative offset tile for the map.
	 *
	 * @return {@link Tile} of where the offset is.
	 */
	public Tile mapOffset() {
		final IClient client = ctx.client();
		if (client == null) {
			return Tile.NIL;
		}
		return new Tile(client.getOffsetX(), client.getOffsetY(), client.getFloor());
	}

	/**
	 * Whether or not the 2-dimension point is within the viewport of the applet.
	 *
	 * @param p The 2-dimensional point to check.
	 * @return {@code true} if it is within bounds, {@code false} otherwise.
	 */
	public boolean inViewport(final Point p) {
		return inViewport(p, resizable());
	}

	/**
	 * Whether or not the 2-dimension point is within the viewport of the applet.
	 *
	 * @param p         The 2-dimensional point to check.
	 * @param resizable true if in resizable mode
	 * @return {@code true} if it is within bounds, {@code false} otherwise.
	 */
	public boolean inViewport(final Point p, final boolean resizable) {
		return pointInViewport(p.x, p.y, resizable);
	}

	/**
	 * Whether or not the game client is resizeable.
	 *
	 * @return {@code true} if it is resizeable, {@code false} otherwise.
	 */
	public boolean resizable() {
		if (ctx.input.inputBounds().width == 765 && ctx.input.inputBounds().height == 503) {
			return false;
		}
		return true;
//		return ctx.widgets.widget(Constants.VIEWPORT_WIDGET >> 16).component(Constants.VIEWPORT_WIDGET & 0xfff).screenPoint().x != 4;
	}

	/**
	 * Whether or not the game tabs are in a bottom line.
	 *
	 * @return {@code true} if they are aligned on the bottom, {@code false} otherwise.
	 */
	public boolean bottomLineTabs() {
		return (ctx.varpbits.varpbit(1055) >>> 8 & 0x1) == 1;
	}

	/**
	 * Whether or not the 2-dimension point is within the viewport of the applet.
	 *
	 * @param x The x-axis value
	 * @param y The y-axis value
	 * @return {@code true} if it is within bounds, {@code false} otherwise.
	 */
	public boolean pointInViewport(final int x, final int y) {
		return pointInViewport(x, y, resizable());
	}

	/**
	 * Whether or not the 2-dimension point is within the viewport of the applet.
	 *
	 * @param x         The x-axis value
	 * @param y         The y-axis value
	 * @param resizable true if client is resizable
	 * @return {@code true} if it is within bounds, {@code false} otherwise.
	 */
	public boolean pointInViewport(final int x, final int y, final boolean resizable) {
		if (ctx.client().isMobile()) {
			return true;
		} else if (resizable) {
			final Dimension d = dimensions();
			return x >= 0 && y >= 0 && (x > 520 || y <= d.height - 170) &&
				(x < d.width - 245 || y < d.height - 340 && y > 170) &&
				x <= (d.width - 1) && y <= (d.height - 1);
		}
		return x >= FIXED_VIEWPORT_START_X && y >= FIXED_VIEWPORT_START_Y && x <= FIXED_VIEWPORT_END_X && y <= FIXED_VIEWPORT_END_Y;
	}

	/**
	 * The {@link HintArrow}.
	 *
	 * @return {@link HintArrow}.
	 */
	public HintArrow hintArrow() {
		//TODO: hint arrow
		return new HintArrow();
	}

	/**
	 * Converts the 3-dimensional tile to a 2-dimensional point on the mini-map component.
	 *
	 * @param tile The tile to convert
	 * @return The point on screen of where the tile would be.
	 */
	public Point tileToMap(final Tile tile) {
		final IClient client = ctx.client();
		if (client == null) {
			return new Point(-1, -1);
		}
		final int rel = ctx.players.local().relative();
		final int angle = client.getMinimapAngle() & 0x7ff;
		final int[] d = {tile.x(), tile.y(), ARRAY_SIN[angle], ARRAY_COS[angle], -1, -1};
		d[0] = (d[0] - client.getOffsetX()) * 4 + 2 - (rel >> 16) / 32;
		d[1] = (d[1] - client.getOffsetY()) * 4 + 2 - (rel & 0xffff) / 32;
		d[4] = d[1] * d[2] + d[3] * d[0] >> 16;
		d[5] = d[2] * d[0] - d[1] * d[3] >> 16;
		final Point centre = mapComponent().centerPoint();
		return new Point(centre.x + d[4], centre.y + d[5]);
	}

	/**
	 * Returns the tile height of the relative 2-dimensional tile. The
	 * 3-dimensional axis is flipped to represent the X axis being horizontal,
	 * Y axis being Vertical, and Z axis to be depth.
	 *
	 * @param relativeX The x-axis value relative to the origin
	 * @param relativeZ The z-axis value relative to the origin
	 * @return The tile height
	 */
	public int tileHeight(final int relativeX, final int relativeZ) {
		final IClient client = ctx.client();
		if (client == null) {
			return 0;
		}
		int floor = client.getFloor();
		int x = relativeX >> 7;
		int y = relativeZ >> 7;
		if (x < 0 || y < 0 || x > 103 || y > 103 ||
			floor < 0 || floor > 3) {
			return 0;
		}
		final byte[][][] meta = client.getLandscapeMeta();
		final int[][][] heights = client.getTileHeights();
		if (meta == null) {
			return 0;
		}
		if (floor < 3 && (meta[1][x][y] & 0x2) == 2) {
			floor++;
		}

		x &= 0x7f;
		y &= 0x7f;
		final int heightStart = x * heights[floor][1 + x][y] + heights[floor][x][y] * (128 - x) >> 7;
		final int heightEnd = (128 - x) * heights[floor][x][1 + y] + x * heights[floor][1 + x][y + 1] >> 7;
		return y * heightEnd + heightStart * (128 - y) >> 7;
	}

	/**
	 * Converts a 3-dimensional point within the overworld to a 2-dimensional point on the
	 * screen. The 3-dimensional axis is flipped to represent the X axis being horizontal,
	 * Y axis being vertical, and Z axis to be depth.
	 *
	 * @param relativeX The x-axis value relative to the origin
	 * @param relativeZ The z-axis value relative to the origin
	 * @param h         The y-axis value, otherwise known as height.
	 * @return The 2-dimensional point on screen.
	 */
	public Point worldToScreen(final int relativeX, final int relativeZ, final int h) {
		final IClient client = ctx.client();
		if (client == null) {
			return new Point(-1, -1);
		}

		return ctx.bot().getMobileClient()
			.map(it -> it.worldToScreen(relativeX, relativeZ, h))
			.orElseGet(() -> worldToScreen(relativeX, relativeZ, h, resizable()));
	}

	public Point worldToScreen(final int relativeX, final int relativeZ, final int h, final boolean resizable) {
		final IClient client = ctx.client();
		if (client == null) {
			return new Point(-1, -1);
		}
		return worldToScreen(relativeX, tileHeight(relativeX, relativeZ), relativeZ, h, resizable);
	}

	/**
	 * Converts a 3-dimensional point within the overworld to a 2-dimensional point on the
	 * screen. The 3-dimensional axis is flipped to represent the X axis being horizontal,
	 * Y axis being vertical, and Z axis to be depth.
	 *
	 * @param relativeX The x-axis value relative to the origin
	 * @param relativeY The y-axis value relative to the origin
	 * @param relativeZ The z-axis value relative to the origin
	 * @param h         The y-axis value, otherwise known as height
	 * @return The 2-dimensional point on screen.
	 */
	public Point worldToScreen(final int relativeX, final int relativeY, final int relativeZ, final int h) {
		return worldToScreen(relativeX, relativeY, relativeZ, h, resizable());
	}

	/**
	 * Converts a 3-dimensional point within the overworld to a 2-dimensional point on the
	 * screen. The 3-dimensional axis is flipped to represent the X axis being horizontal,
	 * Y axis being vertical, and Z axis to be depth.
	 *
	 * @param relativeX The x-axis value relative to the origin
	 * @param relativeY The y-axis value relative to the origin
	 * @param relativeZ The z-axis value relative to the origin
	 * @param h         The y-axis value, otherwise known as height
	 * @param resizable true if the client is in resizable mode
	 * @return The 2-dimensional point on screen.
	 */
	public Point worldToScreen(final int relativeX, final int relativeY, final int relativeZ, final int h, final boolean resizable) {
		final IClient client = ctx.client();
		final Point r = new Point(-1, -1);
		if (client == null) {
			return r;
		}

		if (relativeX < 128 || relativeX > 13056 ||
			relativeZ < 128 || relativeZ > 13056) {
			return r;
		}
		final int floor = client.getFloor();
		if (floor < 0) {
			return r;
		}
		final int height = relativeY - h;
		final int projectedX = relativeX - client.getCameraX(), projectedZ = relativeZ - client.getCameraZ(),
			projectedY = height - client.getCameraY();
		final int pitch = client.getCameraPitch(), yaw = client.getCameraYaw();
		final int[] c = {ARRAY_SIN[yaw], ARRAY_COS[yaw], ARRAY_SIN[pitch], ARRAY_COS[pitch]};
		final int rotatedX = c[0] * projectedZ + c[1] * projectedX >> 16;
		final int rotatedZ = c[1] * projectedZ - c[0] * projectedX >> 16;
		final int rolledY = c[3] * projectedY - c[2] * rotatedZ >> 16;
		final int rolledZ = c[3] * rotatedZ + c[2] * projectedY >> 16;
		if (rolledZ >= 50) {
			int mx = 256, my = 167;
			if (resizable) {
				final Dimension d = dimensions();
				mx = d.width / 2;
				my = d.height / 2;
			}
			final int proj = client.getTileSize();
			return new Point(
				(rotatedX * proj) / rolledZ + mx,
				(rolledY * proj) / rolledZ + my
			);
		}
		return r;
	}

	/**
	 * Returns the component of the mini-map. If the client is not loaded or the mini-map is not
	 * visible, this will return a {@code nil} component.
	 *
	 * @return The component of the mini-map.
	 */
	public Component mapComponent() {
		Widget i = new Widget(ctx, 0);
		if (ctx.client().isMobile()) {
			i = ctx.widgets.widget(601);
		} else if (resizable()) {
			i = ctx.widgets.widget(bottomLineTabs() ? 164 : 161);
		} else {
			i = ctx.widgets.widget(548);
		}

		for (final Component c : i.components()) {
			if (c.contentType() == 1338) {
				return c;
			}
		}
		return new Component(ctx, i, -1);
	}

	/**
	 * Tabs which represent the different interfaces within the user interface of the game.
	 */
	public enum Tab {
		ATTACK("Combat Options", 168),
		STATS("Skills", 898),
		QUESTS(new String[]{"Quest List", "Minigames", "Achievement Diaries", "Kourend Tasks"}, 776, 1052, 1053, 1299),
		INVENTORY("Inventory", 900),
		EQUIPMENT("Worn Equipment", 901),
		PRAYER("Prayer", 902),
		MAGIC("Magic", 780, 1582, 1583, 1584),
		CLAN_CHAT(new String[]{"Clan-channel", "Your Clan", "View another clan"}, 781, 2307, 2308),
		FRIENDS_LIST("Friends List", 782),
		ACCOUNT_MANAGEMENT("Account Management", 1709),
		IGNORED_LIST("Ignore List", 783),
		LOGOUT("Logout", 907, 542),
		SETTINGS("Settings", 908),
		@Deprecated
		OPTIONS(SETTINGS.tips, SETTINGS.textures),
		EMOTES("Emotes", 909),
		MUSIC("Music Player", 910),
		NONE("", -1);
		public final String[] tips;
		public final int[] textures;

		Tab(final String tip, final int... textures) {
			this(new String[]{tip}, textures);
		}

		Tab(final String[] tips, final int... textures) {
			this.tips = tips;
			this.textures = textures;
		}
	}

	/**
	 * An enumeration of the possible cross-hairs in game.
	 */
	public enum Crosshair implements org.powerbot.script.Crosshair {
		NONE, DEFAULT, ACTION
	}

	public enum Keybind {
		NONE(Tab.NONE, -1, -1),
		ATTACK(Tab.ATTACK, 1224, 0),
		STATS(Tab.STATS, 1224, 5),
		QUESTS(Tab.QUESTS, 1224, 10),
		INVENTORY(Tab.INVENTORY, 1224, 15),
		EQUIPMENT(Tab.EQUIPMENT, 1224, 20),
		PRAYER(Tab.PRAYER, 1224, 25),
		MAGIC(Tab.MAGIC, 1225, 0),
		FRIENDS_LIST(Tab.FRIENDS_LIST, 1225, 10),
		IGNORED_LIST(Tab.IGNORED_LIST, 1225, 15),
		LOGOUT(Tab.LOGOUT, 1226, 5),
		SETTINGS(Tab.SETTINGS, 1225, 20),
		@Deprecated
		OPTIONS(SETTINGS.tab, SETTINGS.varpbit, SETTINGS.shift),
		EMOTES(Tab.EMOTES, 1225, 25),
		CLAN_CHAT(Tab.CLAN_CHAT, 1225, 5),
		MUSIC(Tab.MUSIC, 1226, 0);
		private static final int KEY_MASK = 0xf;
		private final Tab tab;
		private final int varpbit, shift;

		Keybind(final Tab tab, final int varpbit, final int shift) {
			this.tab = tab;
			this.varpbit = varpbit;
			this.shift = shift;
		}

		public static Keybind keybind(final Tab tab) {
			for (final Keybind k : Keybind.values()) {
				if (k.tab == tab) {
					return k;
				}
			}
			return NONE;
		}

		public String key(final ClientContext ctx) {
			if (this == NONE) {
				return "";
			}
			int value = ctx.varpbits.varpbit(varpbit);
			if (value < 0) {
				value -= Integer.MIN_VALUE;
			}
			value = ((value >>> shift) & KEY_MASK);
			if (value == 0) {
				return "";
			}
			return value == 13 ? "{VK_ESCAPE}" : "{VK_F" + value + "}";
		}
	}

	public class Settings {
		public boolean escClosingEnabled() {
			return ctx.varpbits.varpbit(1224, 31, 1) == 1;
		}
	}
}
