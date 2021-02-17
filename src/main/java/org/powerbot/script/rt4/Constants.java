package org.powerbot.script.rt4;

import org.powerbot.script.Tile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Constants
 * A utility class holding all the game constants for rt4.
 */
public final class Constants {
	private static final Logger LOGGER = Logger.getLogger("Constants");
	private static final Properties LOCAL;

	static {
		LOCAL = new Properties();

		try {
			LOCAL.load(new InputStreamReader(Objects.requireNonNull(Constants.class.getClassLoader().getResourceAsStream("constants.properties"))));
		} catch (final IOException local) {
			LOGGER.severe("failed to read constants");
		}
	}

	private static int getInt(final String property) {
		return Integer.parseInt(LOCAL.getProperty(property));
	}

	public static final int GAME_LOGIN = getInt("GAME_LOGIN");
	public static final int GAME_LOGGING = getInt("GAME_LOGGING");
	public static final int GAME_LOBBY = getInt("GAME_LOBBY");
	public static final int GAME_LOGGED = getInt("GAME_LOGGED");

	public static final int LOGIN_STATE_WELCOME = getInt("LOGIN_STATE_WELCOME");
	public static final int LOGIN_STATE_PVP_WARNING = getInt("LOGIN_STATE_PVP_WARNING");
	public static final int LOGIN_STATE_ENTER_CREDENTIALS = getInt("LOGIN_STATE_ENTER_CREDENTIALS");
	public static final int LOGIN_STATE_DISCONNECTED = getInt("LOGIN_STATE_DISCONNECTED");

	public static final int BANK_WIDGET = getInt("BANK_WIDGET");
	public static final int BANK_ITEMS = getInt("BANK_ITEMS");
	public static final int BANK_SCROLLBAR = getInt("BANK_SCROLLBAR");
	public static final int BANK_MASTER = getInt("BANK_MASTER");
	public static final int BANK_CLOSE = getInt("BANK_CLOSE");
	public static final int BANK_ITEM = getInt("BANK_ITEM");
	public static final int BANK_NOTE = getInt("BANK_NOTE");
	public static final int BANK_PLACEHOLDERS = getInt("BANK_PLACEHOLDERS");
	public static final int BANK_DEPOSIT_INVENTORY = getInt("BANK_DEPOSIT_INVENTORY");
	public static final int BANK_DEPOSIT_EQUIPMENT = getInt("BANK_DEPOSIT_EQUIPMENT");
	public static final int BANK_QUANTITY_ONE = getInt("BANK_QUANTITY_ONE");
	public static final int BANK_QUANTITY_FIVE = getInt("BANK_QUANTITY_FIVE");
	public static final int BANK_QUANTITY_TEN = getInt("BANK_QUANTITY_TEN");
	public static final int BANK_QUANTITY_X = getInt("BANK_QUANTITY_X");
	public static final int BANK_QUANTITY_ALL = getInt("BANK_QUANTITY_ALL");
	public static final int BANKPIN_WIDGET = getInt("BANKPIN_WIDGET");
	public static final int BANKPIN_PENDING_WIDGET = getInt("BANKPIN_PENDING_WIDGET");
	public static final int BANKPIN_PENDING_COMPONENT = getInt("BANKPIN_PENDING_COMPONENT");
	//varps don't change so we'll leave these
	public static final int BANK_QUANTITY = 1666;
	public static final int BANK_X_VALUE = 304;
	public static final int BANK_TABS = 867;
	public static final int BANK_TABS_HIDDEN = 0xc0000000;
	public static final int BANK_STATE = 115;

	public static final int BANK_COLLECTION_BOX_CLOSE_BUTTON_WIDGET = getInt("BANK_COLLECTION_BOX_CLOSE_BUTTON_WIDGET");
	public static final int BANK_COLLECTION_BOX_CLOSE_BUTTON_COMPONENT = getInt("BANK_COLLECTION_BOX_CLOSE_BUTTON_COMPONENT");
	public static final int BANK_COLLECTION_BOX_CLOSE_BUTTON_COMPONENT_CHILD = getInt("BANK_COLLECTION_BOX_CLOSE_BUTTON_COMPONENT_CHILD");

	public static final int DEPOSITBOX_WIDGET = getInt("DEPOSITBOX_WIDGET");
	public static final int DEPOSITBOX_CLOSE = getInt("DEPOSITBOX_CLOSE");
	public static final int DEPOSITBOX_INVENTORY = getInt("DEPOSITBOX_INVENTORY");
	public static final int DEPOSITBOX_WORN_ITEMS = getInt("DEPOSITBOX_WORN_ITEMS");
	public static final int DEPOSITBOX_LOOT = getInt("DEPOSITBOX_LOOT");
	public static final int DEPOSITBOX_ITEMS = getInt("DEPOSITBOX_ITEMS");

	public static final int EQUIPMENT_WIDGET = getInt("EQUIPMENT_WIDGET");

	public static final int GAME_LOADED = getInt("GAME_LOADED");
	public static final int GAME_LOADING = getInt("GAME_LOADING");

	public static final int LOBBY_WIDGET = getInt("LOBBY_WIDGET");
	public static final int LOBBY_PLAY = getInt("LOBBY_PLAY");

	public static final int LOGOUT_BUTTON_WIDGET = getInt("LOGOUT_BUTTON_WIDGET");
	public static final int LOGOUT_BUTTON_COMPONENT = getInt("LOGOUT_BUTTON_COMPONENT");
	public static final int LOGOUT_DOOR_TEXTURE = getInt("LOGOUT_DOOR_TEXTURE");
	public static final int WORLD_SELECTOR_WIDGET = getInt("WORLD_SELECTOR_WIDGET");

	public static final int INVENTORY_WIDGET = getInt("INVENTORY_WIDGET");
	public static final int INVENTORY_ITEMS = getInt("INVENTORY_ITEMS");
	//these don't change
	public static final int INVENTORY_ITEM_WIDTH = 42;
	public static final int INVENTORY_ITEM_HEIGHT = 36;
	//DIFFERENCE BETWEEN CENTER POINTS
	public static final int INVENTORY_ITEM_X_DIFFERENCE = 42;
	public static final int INVENTORY_ITEM_Y_DIFFERENCE = 36;
	//DIFFERENCE FROM INVENTORY COMPONENT TO FIRST INVENTORY SLOT
	public static final int INVENTORY_ITEM_BASE_X_DIFFERENCE = 18;
	public static final int INVENTORY_ITEM_BASE_Y_DIFFERENCE = 16;
	public static final int INVENTORY_SIZE = 28;

	public static final int INVENTORY_BANK_WIDGET = getInt("INVENTORY_BANK_WIDGET");
	public static final int INVENTORY_BANK_ITEMS = getInt("INVENTORY_BANK_ITEMS");
	public static final int INVENTORY_GRAND_EXCHANGE_WIDGET = getInt("INVENTORY_GRAND_EXCHANGE_WIDGET");
	public static final int INVENTORY_GRAND_EXCHANGE_ITEMS = getInt("INVENTORY_GRAND_EXCHANGE_ITEMS");
	public static final int INVENTORY_SHOP_WIDGET = getInt("INVENTORY_SHOP_WIDGET");
	public static final int INVENTORY_SHOP_ITEMS = getInt("INVENTORY_SHOP_ITEMS");
	public static final int[][] INVENTORY_ALTERNATIVES = {
		{INVENTORY_BANK_WIDGET, INVENTORY_BANK_ITEMS},
		{INVENTORY_GRAND_EXCHANGE_WIDGET, INVENTORY_GRAND_EXCHANGE_ITEMS},
		{INVENTORY_SHOP_WIDGET, INVENTORY_SHOP_ITEMS}
	};

	public static final int MOVEMENT_MAP = getInt("MOVEMENT_MAP");
	public static final int MOVEMENT_RUN_ENERGY = getInt("MOVEMENT_RUN_ENERGY");
	public static final int MOVEMENT_RUNNING = getInt("MOVEMENT_RUNNING");
	public static final int MOVEMENT_QUICK_PRAYER = getInt("MOVEMENT_QUICK_PRAYER");
	public static final int MOVEMENT_SPECIAL_ATTACK = getInt("MOVEMENT_SPECIAL_ATTACK");
	public static final int MOVEMENT_SPECIAL_ENERGY = getInt("MOVEMENT_SPECIAL_ENERGY");

	//these don't change
	public static final int[] SKILLS_XP = {0, 0, 83, 174, 276, 388, 512, 650, 801, 969, 1154, 1358, 1584, 1833, 2107,
		2411, 2746, 3115, 3523, 3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363, 14833,
		16456, 18247, 20224, 22406, 24815, 27473, 30408, 33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983,
		75127, 83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040, 203254, 224466, 247886, 273742,
		302288, 333804, 368599, 407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445, 899257, 992895,
		1096278, 1210421, 1336443, 1475581, 1629200, 1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594,
		3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253, 7195629, 7944614, 8771558, 9684577, 10692629,
		11805606, 13034431, 14391160, 15889109, 17542976, 19368992, 21385073, 23611006, 26068632, 28782069,
		31777943, 35085654, 38737661, 42769801, 47221641, 52136869, 57563718, 63555443, 70170840, 77474828,
		85539082, 94442737, 104273167};
	public static final int SKILLS_ATTACK = 0;
	public static final int SKILLS_DEFENSE = 1;
	public static final int SKILLS_STRENGTH = 2;
	public static final int SKILLS_HITPOINTS = 3;
	public static final int SKILLS_RANGE = 4;
	public static final int SKILLS_PRAYER = 5;
	public static final int SKILLS_MAGIC = 6;
	public static final int SKILLS_COOKING = 7;
	public static final int SKILLS_WOODCUTTING = 8;
	public static final int SKILLS_FLETCHING = 9;
	public static final int SKILLS_FISHING = 10;
	public static final int SKILLS_FIREMAKING = 11;
	public static final int SKILLS_CRAFTING = 12;
	public static final int SKILLS_SMITHING = 13;
	public static final int SKILLS_MINING = 14;
	public static final int SKILLS_HERBLORE = 15;
	public static final int SKILLS_AGILITY = 16;
	public static final int SKILLS_THIEVING = 17;
	public static final int SKILLS_SLAYER = 18;
	public static final int SKILLS_FARMING = 19;
	public static final int SKILLS_RUNECRAFTING = 20;
	public static final int SKILLS_HUNTER = 21;
	public static final int SKILLS_CONSTRUCTION = 22;

	//this won't change
	public static final int VIEWPORT_WIDGET = 548 << 16 | 12;
	public static final int RESIZABLE_VIEWPORT_WIDGET = 161;
	public static final int RESIZABLE_VIEWPORT_BOTTOM_LINE_WIDGET = 164;
	public static final int RESIZABLE_VIEWPORT_COMPONENT = 13;

	@Deprecated
	public static final int CHAT_NPC = 231;
	@Deprecated
	public static final int CHAT_PLAYER = 217;
	@Deprecated
	public static final int CHAT_CONTINUE = 2;

	public static final int CHAT_INPUT = getInt("CHAT_INPUT");
	public static final int CHAT_VIEWPORT = getInt("CHAT_VIEWPORT");
	public static final int CHAT_INPUT_TEXT = getInt("CHAT_INPUT_TEXT");
	public static final int CHAT_WIDGET = getInt("CHAT_WIDGET");
	public static final int[][] CHAT_CONTINUES = {
		{229, 2},
		{231, 3}, //npc
		{217, 3}, //player
		{229, 1},  //acknowledge
		{233, 3},  //skill level-up
		{11, 3},   //at tutorials island
	};
	public static final int[] CHAT_OPTIONS = {
		1, 2, 3, 4, 5
	};

	public static final int PRAYER_QUICK_SELECT = getInt("PRAYER_QUICK_SELECT");
	public static final int PRAYER_QUICK_SELECT_CONTAINER = getInt("PRAYER_QUICK_SELECT_CONTAINER");
	public static final int PRAYER_SELECT = getInt("PRAYER_SELECT");
	public static final int PRAYER_QUICK_SELECTED = getInt("PRAYER_QUICK_SELECTED");
	public static final int PRAYER_QUICK_SELECTION = getInt("PRAYER_QUICK_SELECTION");
	public static final int PRAYER_SELECTION = getInt("PRAYER_SELECTION");

	//varps don't change
	public static final int SPELLBOOK_VARPBIT = 439;
	public static final int SPELLBOOK_WIDGET = getInt("SPELLBOOK_WIDGET");

	public static final int COMBAT_OPTIONS_WIDGET = getInt("COMBAT_OPTIONS_WIDGET");

	public static final String[] BANK_NPCS = {"Banker", "Ghost banker", "Banker tutor", "Sirsal Banker", "Nardah Banker", "Gnome banker", "Fadli", "Emerald Benedict", "Arnold Lydspor", "Cornelius", "Gundai", "Jade", "TzHaar-Ket-Yil", "TzHaar-Ket-Zuh", "Jumaane", "Magnus Gram", "Yusuf"};
	public static final String[] BANK_CHESTS = {"Bank chest", "Bank Chest-wreck", "Open chest"};
	public static final String[] BANK_BOOTHS = {"Bank booth"};
	public static final Tile[] BANK_UNREACHABLES = new Tile[]{
		new Tile(3187, 3446, 0), new Tile(3088, 3242, 0),
		new Tile(3096, 3242, 0), new Tile(3096, 3241, 0),
		new Tile(3096, 3245, 0), new Tile(3091, 3241, 0),
		new Tile(2584, 3418, 0)
	};

	public static final int[] WIDGETCLOSER_TRADE_ITEMS = {
		335 << 16 | 15,
	};
	//Textures of "X" close button
	public static final int[] CLOSE_BUTTON_TEXTURES = {831, 535, 537, 539};
}
