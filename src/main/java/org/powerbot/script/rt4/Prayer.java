package org.powerbot.script.rt4;

import org.powerbot.script.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Prayer
 */
public class Prayer extends ClientAccessor {
	public Prayer(final ClientContext ctx) {
		super(ctx);
	}

	public int prayerPoints() {
		return StringUtils.parseInt(ctx.widgets.component(Constants.MOVEMENT_MAP, Constants.MOVEMENT_QUICK_PRAYER + 1).text());
	}

	@Deprecated
	public int points() {
		return prayerPoints();
	}

	public boolean prayersActive() {
		return ctx.varpbits.varpbit(Constants.PRAYER_SELECTION) > 0;
	}

	@Deprecated
	public boolean praying() {
		return prayersActive();
	}

	public boolean quickPrayer() {
		return ctx.varpbits.varpbit(Constants.PRAYER_QUICK_SELECTED, 0x1) == 1;
	}

	public boolean quickPrayer(final boolean on) {
		return quickPrayer() == on || (ctx.widgets.component(Constants.MOVEMENT_MAP, Constants.MOVEMENT_QUICK_PRAYER).click() && Condition.wait(() -> quickPrayer() == on, 300, 6));
	}

	public boolean quickSelectionActive() {
		return ctx.widgets.widget(Constants.PRAYER_QUICK_SELECT).component(0).valid();
	}

	public boolean prayerActive(final Effect effect) {
		return ctx.varpbits.varpbit(Constants.PRAYER_SELECTION, effect.quickSelectIndex(), 0x1) == 1;
	}

	public boolean prayer(final Effect effect, final boolean active) {
		if (ctx.skills.realLevel(Constants.SKILLS_PRAYER) < effect.level()) {
			return false;
		}
		if (prayerActive(effect) == active) {
			return true;
		}
		if (ctx.game.tab(Game.Tab.PRAYER)) {
			return ctx.widgets.component(Constants.PRAYER_SELECT, effect.index()).interact(active ? "Activate" : "Deactivate");
		}
		return prayerActive(effect) == active;
	}

	public boolean quickSelection(final boolean quick) {
		if (quickSelectionActive() == quick) {
			return true;
		}
		if (quick) {
			if (!ctx.widgets.component(Constants.MOVEMENT_MAP, Constants.MOVEMENT_QUICK_PRAYER).interact("Setup", "Quick-prayers")) {
				return false;
			}
		} else {
			if (!ctx.game.tab(Game.Tab.PRAYER) || !ctx.widgets.component(Constants.PRAYER_QUICK_SELECT, 6).interact("Done")) {
				return false;
			}
		}

		return Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return quickSelectionActive() == quick;
			}
		}, 150, 10);
	}

	public boolean prayerQuick(final Effect effect) {
		return ctx.varpbits.varpbit(Constants.PRAYER_QUICK_SELECTION, effect.quickSelectIndex(), 0x1) == 1;
	}

	public Effect[] activePrayers() {
		final Set<Effect> active = new LinkedHashSet<>();
		for (final Effect effect : Effect.values()) {
			if (prayerActive(effect)) {
				active.add(effect);
			}
		}
		return active.toArray(new Effect[0]);
	}

	public Effect[] quickPrayers() {
		final Set<Effect> quick = new LinkedHashSet<>();
		for (final Effect effect : Effect.values()) {
			if (prayerQuick(effect)) {
				quick.add(effect);
			}
		}
		return quick.toArray(new Effect[0]);
	}


	public boolean quickPrayers(final Effect... effects) {
		if (!quickSelectionActive()) {
			if (!quickSelection(true)) {
				return false;
			}
		}
		if (quickSelectionActive() && ctx.game.tab(Game.Tab.PRAYER)) {
			for (final Effect effect : effects) {
				if (prayerQuick(effect)) {
					continue;
				}

				if (ctx.widgets.component(Constants.PRAYER_QUICK_SELECT, Constants.PRAYER_QUICK_SELECT_CONTAINER).component(effect.quickSelectIndex()).interact("Toggle")) {
					Condition.sleep();
				}
			}

			for (final Effect effect : quickPrayers()) {
				if (prayerQuick(effect) && !search(effects, effect)) {
					if (ctx.widgets.component(Constants.PRAYER_QUICK_SELECT, Constants.PRAYER_QUICK_SELECT_CONTAINER).component(effect.quickSelectIndex()).interact("Toggle")) {
						Condition.sleep();
					}
				}
			}
		}

		for (int i = 0; i < 3; i++) {
			if (!quickSelectionActive()) {
				break;
			}
			quickSelection(false);
		}
		return !quickSelectionActive();
	}

	private boolean search(final Effect[] effects, final Effect effect) {
		for (final Effect e : effects) {
			if (e.index() == effect.index()) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Order of effects is based off the quick prayer setup component ordering.
	 * Indexes are based off the main prayer interface components.
	 */
	public enum Effect {
		THICK_SKIN(5, 1),
		BURST_OF_STRENGTH(6, 4),
		CLARITY_OF_THOUGHT(7, 7),
		ROCK_SKIN(8, 10),
		SUPERHUMAN_STRENGTH(9, 13),
		IMPROVED_REFLEXES(10, 16),
		RAPID_RESTORE(11, 19),
		RAPID_HEAL(12, 22),
		PROTECT_ITEM(13, 25),
		STEEL_SKIN(14, 28),
		ULTIMATE_STRENGTH(15, 31),
		INCREDIBLE_REFLEXES(16, 34),
		PROTECT_FROM_MAGIC(17, 37),
		PROTECT_FROM_MISSILES(18, 40),
		PROTECT_FROM_MELEE(19, 43),
		RETRIBUTION(20, 46),
		REDEMPTION(21, 49),
		SMITE(22, 52),
		SHARP_EYE(23, 8),
		MYSTIC_WILL(24, 9),
		HAWK_EYE(25, 26),
		MYSTIC_LORE(26, 27),
		EAGLE_EYE(27, 44),
		MYSTIC_MIGHT(28, 45),
		RIGOUR(31, 74),
		CHIVALRY(29, 60),
		PIETY(30, 70),
		AUGURY(32, 77),
		PRESERVE(33, 55);
		private final int index;
		private final int level;

		Effect(final int index, final int level) {
			this.index = index;
			this.level = level;
		}

		public int index() {
			return index;
		}

		public int level() {
			return level;
		}

		public int quickSelectIndex() {
			return ordinal();
		}
	}
}
