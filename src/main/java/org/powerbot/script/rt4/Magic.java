package org.powerbot.script.rt4;

import org.powerbot.script.rt4.Game.*;

/**
 * Magic interface
 */
public class Magic extends ClientAccessor {

	public Magic(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * Retrieves the current spell book that the user is on.
	 *
	 * @return The current spell book.
	 */
	public Book book() {
		final int varp = ctx.varpbits.varpbit(Constants.SPELLBOOK_VARPBIT) & 0x3;

		for (final Book b : Book.values()) {
			if (varp == b.varp) {
				return b;
			}
		}

		return Book.NIL;
	}

	/**
	 * Retrieves the current spell selected in the spell book. If no spell is
	 * currently being casted, Spell.NIL will be returned instead.
	 *
	 * @return The magic spell being casted.
	 */
	public MagicSpell magicspell() {
		final Book book = book();

		for (final MagicSpell spell : book.spells()) {
			final Component c = component(spell);
			if (c.valid() && c.borderThickness() == 2) {
				return spell;
			}
		}

		return Spell.NIL;
	}

	/**
	 * Validates that a player has a specific spell selected.
	 *
	 * @param spell The spell to validate.
	 * @return {@code true} if the spell is currently selected, {@code false}
	 * otherwise.
	 */
	public boolean casting(final MagicSpell spell) {
		return magicspell() == spell;
	}

	/**
	 * Deprecated method. This method returns the current spell selected in the
	 * spell book only if the user is in the Modern spell book. All other cases
	 * are not valid. If a spell cannot be fond, Spell.NIL will be returned
	 * instead.
	 *
	 * @return The magic spell being casted.
	 * @see Magic#magicspell()
	 */
	@Deprecated
	public Spell spell() {
		final MagicSpell spell = magicspell();
		if (!(spell instanceof Spell)) {
			return Spell.NIL;
		} else {
			return (Spell) spell;
		}
	}

	/**
	 * Attempts to cast the given MagicSpell. This method will switch to the
	 * spell book tab if it is not already selected. A boolean value will be
	 * returned representing whether the spell's component was successfully
	 * clicked or not.
	 *
	 * @param spell The spell to cast.
	 * @return {@code true} if the spell component was successfully clicked,
	 * {@code false} otherwise.
	 */
	public boolean cast(final MagicSpell spell) {
		if (!ctx.game.tab(Game.Tab.MAGIC)) {
			return false;
		}

		final Component c = component(spell);
		return c.visible() && c.click(cmd -> cmd.action.equals("Cast") || cmd.action.equals("Reanimate") || cmd.action.equals("Resurrect"));
	}

	/**
	 * Attempts to cast the given MagicSpell with specified action (to support
	 * redirected teleports). This method will switch to the spell book tab if
	 * it is not already selected. A boolean value will be returned representing
	 * whether the spell's component was successfully clicked or not.
	 *
	 * @param spell The spell to cast.
	 * @param action the action type
	 * @return {@code true} if the spell component was successfully clicked,
	 * {@code false} otherwise.
	 */
	public boolean cast(final MagicSpell spell, final String action) {
		if (!ctx.game.tab(Game.Tab.MAGIC)) {
			return false;
		}

		final Component c = component(spell);
		return c.visible() && c.click(cmd -> cmd.action.equals(action));
	}

	/**
	 * Returns a boolean value representing whether a spell is ready to be cast.
	 * The player must be in the magic tab and have the required level, runes,
	 * and items for the cast.
	 *
	 * @param spell The spell to validate.
	 * @return {@code true} if it is ready to be cast, {@code false} otherwise.
	 */
	public boolean ready(final MagicSpell spell) {
		return ctx.game.tab() == Tab.MAGIC && component(spell).textureId() != spell.texture();
	}

	/**
	 * Returns the component holding the MagicSpell. If the component cannot be
	 * found, an empty component will be returned.
	 *
	 * @param spell The spell to retrieve.
	 * @return The component of the spell.
	 */
	public Component component(final MagicSpell spell) {
		final Widget bookWidget = ctx.widgets.widget(Book.widget());

		for (final Component c : bookWidget.components()) {
			final int texture = c.textureId();
			final int spellOff = spell.texture();
			final int spellOn = spellOff - (spellOff == 406 || spell.book() != Book.ARCEUUS ? 50 : 25);
			if (texture == spellOff || texture == spellOn) {
				return c;
			}
		}

		return bookWidget.component(-1);
	}

	/**
	 * Parent interface class for all magical spells in any spell book.
	 */
	public interface MagicSpell {

		/**
		 * Retrieve the spell book that this magic spell is in.
		 *
		 * @return A spell book
		 */
		Book book();

		/**
		 * Retrieve the magic level required to cast this magic spell.
		 *
		 * @return The integer magic level required
		 */
		int level();

		/**
		 * Retrieve the off texture (texture seen if the spell is unavailable)
		 * of this magic spell.
		 *
		 * @return The integer off texture
		 */
		int texture();

	}

	/**
	 * Represents all the spells in the Modern spell book.
	 */
	public enum Spell implements MagicSpell {

		NIL(Integer.MIN_VALUE, Integer.MIN_VALUE),
		HOME_TELEPORT(0, 4, 406),
		WIND_STRIKE(1, 5, 65),
		CONFUSE(3, 6, 66),
		ENCHANT_CROSSBOW_BOLT_OPAL(4, 7),
		WATER_STRIKE(5, 8, 67),
		ENCHANT_LEVEL_1_JEWELLERY(7, 9, 68),
		ENCHANT_CROSSBOW_BOLT_SAPPHIRE(7, 7),
		EARTH_STRIKE(9, 10, 69),
		WEAKEN(11, 11, 70),
		FIRE_STRIKE(13, 12, 71),
		ENCHANT_CROSSBOW_BOLT_JADE(14, 7),
		BONES_TO_BANANAS(15, 13, 72),
		WIND_BOLT(17, 14, 73),
		CURSE(19, 15, 74),
		BIND(20, 16, 369),
		LOW_LEVEL_ALCHEMY(21, 17, 75),
		WATER_BOLT(23, 18, 76),
		ENCHANT_CROSSBOW_BOLT_PEARL(24, 7),
		VARROCK_TELEPORT(25, 19, 77),
		ENCHANT_LEVEL_2_JEWELLERY(27, 20, 78),
		ENCHANT_CROSSBOW_BOLT_EMERALD(27, 7),
		EARTH_BOLT(29, 21, 79),
		ENCHANT_CROSSBOW_BOLT_RED_TOPAZ(29, 7),
		LUMBRIDGE_TELEPORT(31, 22, 80),
		TELEKINETIC_GRAB(33, 23, 81),
		FIRE_BOLT(25, 24, 82),
		FALADOR_TELEPORT(37, 25, 83),
		CRUMBLE_UNDEAD(39, 26, 84),
		TELEPORT_TO_HOUSE(40, 27, 405),
		WIND_BLAST(41, 28, 85),
		SUPERHEAT_ITEM(43, 29, 86),
		CAMELOT_TELEPORT(45, 30, 87),
		WATER_BLAST(47, 31, 88),
		ENCHANT_LEVEL_3_JEWELLERY(49, 32, 89),
		ENCHANT_CROSSBOW_BOLT_RUBY(49, 7),
		IBAN_BLAST(50, 33, 103),
		SNARE(50, 34, 370),
		MAGIC_DART(50, 35, 374),
		ARDOUGNE_TELEPORT(51, 36, 104),
		EARTH_BLAST(51, 37, 90),
		HIGH_ALCHEMY(55, 38, 91),
		CHARGE_WATER_ORB(56, 39, 92),
		ENCHANT_LEVEL_4_JEWELLERY(57, 40, 93),
		ENCHANT_CROSSBOW_BOLT_DIAMOND(57, 7),
		WATCHTOWER_TELEPORT(58, 41, 105),
		FIRE_BLAST(59, 42, 94),
		CHARGE_EARTH_ORB(60, 43, 95),
		BONES_TO_PEACHES(60, 44, 404),
		SARADOMIN_STRIKE(60, 45, 111),
		CLAWS_OF_GUTHIX(60, 46, 110),
		FLAMES_OF_ZAMORAK(60, 47, 109),
		TROLLHEIM_TELEPORT(61, 48, 373),
		WIND_WAVE(62, 49, 96),
		CHARGE_FIRE_ORB(63, 50, 97),
		TELEPORT_APE_ATOLL(64, 51, 407),
		WATER_WAVE(65, 52, 98),
		CHARGE_AIR_ORB(66, 53, 99),
		VULNERABILITY(66, 54, 106),
		ENCHANT_LEVEL_5_JEWELLERY(68, 55, 100),
		ENCHANT_CROSSBOW_BOLT_DRAGONSTONE(68, 7),
		TELEPORT_KOUREND(69, 56, 410),
		EARTH_WAVE(70, 57, 101),
		ENFEEBLE(73, 58, 107),
		TELEOTHER_LUMBRIDGE(74, 59, 399),
		FIRE_WAVE(75, 60, 102),
		ENTANGLE(79, 61, 371),
		STUN(80, 62, 108),
		CHARGE(80, 63, 372),
		WIND_SURGE(81, 64, 412),
		TELEOTHER_FALADOR(82, 65, 400),
		WATER_SURGE(85, 66, 413),
		TELE_BLOCK(85, 67, 402),
		TELEPORT_TO_BOUNTY_TARGET(90, 68, 409),
		ENCHANT_LEVEL_6_JEWELLERY(87, 69, 403),
		ENCHANT_CROSSBOW_BOLT_ONYX(87, 7),
		TELEOTHER_CAMELOT(90, 70, 401),
		EARTH_SURGE(90, 71, 414),
		ENCHANT_LEVEL_7_JEWELLERY(93, 72, 411),
		FIRE_SURGE(95, 73, 415);

		private final int level, component, offTexture;

		Spell(final int level, final int component) {
			this(level, component, Integer.MIN_VALUE);
		}

		Spell(final int level, final int component, final int offTexture) {
			this.level = level;
			this.component = component;
			this.offTexture = offTexture;
		}

		public Book book() {
			return Book.MODERN;
		}

		public int level() {
			return level;
		}

		public int texture() {
			return offTexture;
		}

		/**
		 * Deprecated function. Retrieves the index component in the magic
		 * libary widget that the spell is located in.
		 *
		 * @return Integer of component index
		 * @see Magic#component(MagicSpell)
		 */
		@Deprecated
		public int component() {
			return component;
		}
	}

	/**
	 * Represents all the spells in the Ancient spell book.
	 */
	public enum AncientSpell implements MagicSpell {

		HOME_TELEPORT(1, 406),
		SMOKE_RUSH(50, 379),
		SHADOW_RUSH(52, 387),
		PADEWWA_TELEPORT(54, 391),
		BLOOD_RUSH(56, 383),
		ICE_RUSH(58, 375),
		SENNTISTEN_TELEPORT(60, 392),
		SMOKE_BURST(62, 380),
		SHADOW_BURST(64, 388),
		KHARYLL_TELEPORT(66, 393),
		BLOOD_BURST(68, 384),
		ICE_BURST(70, 376),
		LASSAR_TELEPORT(72, 394),
		SMOKE_BLITZ(74, 381),
		SHADOW_BLITZ(76, 389),
		DAREEYAK_TELEPORT(78, 395),
		BLOOD_BLITZ(80, 385),
		ICE_BLITZ(82, 377),
		CARRALLANGAR_TELEPORT(84, 396),
		TELEPORT_TO_BOUNTY_TARGET(85, 409),
		SMOKE_BARRAGE(86, 382),
		SHADOW_BARRAGE(88, 390),
		ANNAKARL_TELEPORT(90, 397),
		BLOOD_BARRAGE(92, 386),
		ICE_BARRAGE(94, 378),
		GHORROCK_TELEPORT(96, 398);

		private final int level, offTexture;

		AncientSpell(final int level, final int offTexture) {
			this.level = level;
			this.offTexture = offTexture;
		}

		@Override
		public Book book() {
			return Book.ANCIENT;
		}

		@Override
		public int level() {
			return level;
		}

		@Override
		public int texture() {
			return offTexture;
		}
	}

	/**
	 * Represents all the spells in the Lunar spell book.
	 */
	public enum LunarSpell implements MagicSpell {

		HOME_TELEPORT(0, 406),
		BAKE_PIE(65, 593),
		GEOMANCY(65, 613),
		CURE_PLANT(66, 617),
		MONSTER_EXAMINE(66, 627),
		NPC_CONTACT(67, 618),
		CURE_OTHER(68, 609),
		HUMIDIFY(68, 628),
		MOONCLAN_TELEPORT(69, 594),
		TELE_GROUP_MOONCLAN(70, 619),
		CURE_ME(71, 612),
		OURANIA_TELEPORT(71, 636),
		HUNTER_KIT(71, 629),
		WATERBIRTH_TELEPORT(72, 595),
		TELE_GROUP_WATERBIRTH(73, 620),
		CURE_GROUP(74, 615),
		STAT_SPY(75, 626),
		BARBARIAN_TELEPORT(75, 597),
		TELE_GROUP_BARBARIAN(76, 621),
		SPIN_FLAX(77, 635),
		SUPERGLASS_MAKE(77, 598),
		TAN_LEATHER(78, 633),
		KHAZARD_TELEPORT(78, 599),
		TELE_GROUP_KHAZARD(79, 622),
		DREAM(79, 630),
		STRING_JEWELLERY(80, 600),
		STAT_RESTORE_POT_SHARE(81, 604),
		MAGIC_IMBUE(82, 602),
		FERTILE_SOIL(83, 603),
		BOOST_POTION_SHARE(84, 601),
		FISHING_GUILD_TELEPORT(85, 605),
		TELEPORT_TO_BOUNTY_TARGET(85, 409),
		TELE_GROUP_FISHING_GUILD(86, 623),
		PLANK_MAKE(86, 631),
		CATHERBY_TELEPORT(87, 606),
		TELE_GROUP_CATHERBY(88, 624),
		ICE_PLATEAU_TELEPORT(89, 607),
		RECHARGE_DRAGONSTONE(89, 634),
		TELE_GROUP_ICE_PLATEAU(90, 625),
		ENERGY_TRANSFER(91, 608),
		HEAL_OTHER(92, 610),
		VENGEANCE_OTHER(93, 611),
		VENGEANCE(94, 614),
		HEAL_GROUP(95, 616),
		SPELL_BOOK_SWAP(96, 632);

		private final int level, offTexture;

		LunarSpell(final int level, final int offTexture) {
			this.level = level;
			this.offTexture = offTexture;
		}

		@Override
		public Book book() {
			return Book.LUNAR;
		}

		@Override
		public int level() {
			return level;
		}

		@Override
		public int texture() {
			return offTexture;
		}
	}

	/**
	 * Represents all the spells in the Arceuus spell book.
	 */
	public enum ArceuusSpell implements MagicSpell {

		HOME_TELEPORT(0, 406),
		REANIMATE_GOBLIN(3, 1272),
		LUMBRIDGE_GRAVEYARD_TELEPORT(6, 1294),
		REANIMATE_MONKEY(7, 1289),
		REANIMATE_IMP(12, 1283),
		REANIMATE_MINOTAUR(16, 1284),
		DRAYNOR_MANOR_TELEPORT(17, 1295),
		REANIMATE_SCORPION(19, 1282),
		REANIMATE_BEAR(21, 1281),
		REANIMATE_UNICORN(22, 1285),
		REANIMATE_DOG(26, 1293),
		MIND_ALTAR_TELEPORT(28, 1296),
		REANIMATE_CHAOS_DRUID(30, 1276),
		RESPAWN_TELEPORT(34, 1319),
		REANIMATE_GIANT(37, 1280),
		SALVE_GRAVEYARD_TELEPORT(40, 1320),
		REANIMATE_OGRE(40, 1279),
		REANIMATE_ELF(43, 1275),
		REANIMATE_TROLL(46, 1277),
		FENKENSTRAIN_CASTLE_TELEPORT(46, 1321),
		REANIMATE_HORROR(52, 1291),
		REANIMATE_KALPHITE(57, 1286),
		WEST_ARDOUGNE_TELEPORT(61, 1322),
		REANIMATE_DAGANNOTH(62, 1278),
		REANIMATE_BLOODVELD(65, 1292),
		HARMONY_ISLAND_TELEPORT(65, 1323),
		REANIMATE_TZHAAR(69, 1287),
		CEMETERY_TELEPORT(71, 1324),
		REANIMATE_DEMON(72, 1273),
		REANIMATE_AVIANSIE(78, 1288),
		RESURRECT_CROPS(78, 1327),
		BARROWS_TELEPORT(83, 1325),
		REANIMATE_ABYSSAL_CREATURE(85, 1290),
		APE_ATOLL_TELEPORT(90, 1326),
		REANIMATE_DRAGON(93, 1274);

		private final int level, offTexture;

		ArceuusSpell(final int level, final int offTexture) {
			this.level = level;
			this.offTexture = offTexture;
		}

		@Override
		public Book book() {
			return Book.ARCEUUS;
		}

		@Override
		public int level() {
			return level;
		}

		@Override
		public int texture() {
			return offTexture;
		}
	}

	/**
	 * A spell book enum that wraps all the magic spells in each of the
	 * different spell books.
	 */
	public enum Book {

		/**
		 * Wraps all the spells in the Modern spell book.
		 */
		MODERN(Spell.values()),

		/**
		 * Wraps all the spells in the Ancient spell book.
		 */
		ANCIENT(AncientSpell.values()),

		/**
		 * Wraps all the spells in the Lunar spell book.
		 */
		LUNAR(LunarSpell.values()),

		/**
		 * Wraps all the spells in the Arceuus spell book.
		 */
		ARCEUUS(ArceuusSpell.values()),

		/**
		 * A NIL spell book with no active spells.
		 */
		NIL(new MagicSpell[]{});

		@Deprecated
		public final int widget = widget();

		private final int varp;
		private final MagicSpell[] spells;

		Book(final MagicSpell[] spells) {
			this.varp = ordinal();
			this.spells = spells;
		}

		/**
		 * Return the integer constant for the widget of the spell book.
		 *
		 * @return An integer constant
		 */
		public static int widget() {
			return Constants.SPELLBOOK_WIDGET;
		}

		/**
		 * Return an array of MagicSpell that are featured in the spell book.
		 *
		 * @return An array of MagicSpell
		 */
		public final MagicSpell[] spells() {
			return spells;
		}
	}
}
