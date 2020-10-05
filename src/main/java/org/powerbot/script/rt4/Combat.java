package org.powerbot.script.rt4;

import org.powerbot.script.*;

/**
 * Combat
 * A utility class with methods for parsing widget and varpbit values.
 */
public class Combat extends ClientAccessor {
	private Component specialAttackComp = null;
	private Component retaliateComp = null;

	public Combat(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * Returns your player's current health in percentage from max health (0-100%).
	 *
	 * @return player's current health in percentage.
	 */
	public int healthPercent() {
		return (int) (((double) health() / maxHealth()) * 100);
	}

	/**
	 * Returns your player's current health.
	 *
	 * @return player's current health.
	 */
	public int health() {
		return ctx.skills.level(Constants.SKILLS_HITPOINTS);
	}

	/**
	 * Returns your player's max health.
	 *
	 * @return player's max health.
	 */
	public int maxHealth() {
		return ctx.skills.realLevel(Constants.SKILLS_HITPOINTS);
	}

	/**
	 * The special attack percentage.
	 *
	 * @return The percentage (represented as 0-100) of the player's special attack.
	 */
	public int specialPercentage() {
		return ctx.varpbits.varpbit(300) / 10;
	}

	/**
	 * Whether or not the player has a special attack queued.
	 *
	 * @return {@code true} if the player can execute a special attack, {@code false} otherwise.
	 */
	public boolean specialAttack() {
		return ctx.varpbits.varpbit(301) == 1;
	}

	/**
	 * Whether or not the player is in a multi-combat area.
	 *
	 * @return {@code true} if within a multi-combat area, {@code false} otherwise.
	 */
	public boolean inMultiCombat() {
		return ctx.varpbits.varpbit(1021, 5, 0x1) == 1;
	}

	/**
	 * Executes a special attack.
	 *
	 * @param select Whether or not to select the percentage bar.
	 * @return {@code true} if the special attack is selected, {@code false}
	 * otherwise.
	 */
	public boolean specialAttack(final boolean select) {
		if (specialAttack() == select) {
			return true;
		}
		final Component c;
		if (ctx.components.select(false,Constants.MOVEMENT_MAP).select(component -> component.index() == Constants.MOVEMENT_SPECIAL_ATTACK).texture(1607).isEmpty()) {
			if (!ctx.game.tab(Game.Tab.ATTACK)) {
				return false;
			}
			c = specialAttackComp == null ? (specialAttackComp = ctx.components.select(Constants.COMBAT_OPTIONS_WIDGET).textContains("Special attack:").poll()) : specialAttackComp;
		} else {
			c = ctx.components.poll();
		}
		final int current = specialPercentage();
		return c != null && c.visible() && c.click() && Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return specialAttack() == select || specialPercentage() != current;
			}
		}, 300, 6);
	}

	/**
	 * Whether or not the auto-retaliate is turned on.
	 *
	 * @return {@code true} if auto-retaliate is on, {@code false} otherwise.
	 */
	public boolean autoRetaliate() {
		return ctx.varpbits.varpbit(172, 0, 0x1) == 0;
	}

	/**
	 * Sets the desired state of auto-retaliate
	 *
	 * @param set Whether or not to set the auto-retaliate.
	 * @return {@code true} if auto-retaliate was set to desired state, {@code false} otherwise.
	 */
	public boolean autoRetaliate(final boolean set) {
		if (autoRetaliate() == set) {
			return true;
		}
		if (!ctx.game.tab(Game.Tab.ATTACK)) {
			return false;
		}
		final Component c = retaliateComp == null ? (retaliateComp = ctx.components.select(Constants.COMBAT_OPTIONS_WIDGET).textContains("Auto Retaliate").poll()) : retaliateComp;
		return c != null && c.visible() && c.click() && Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return autoRetaliate() == set;
			}
		}, 300, 6);
	}

	/**
	 * Gets the selected combat {@link Style}.
	 *
	 * @return currently selected {@link Style}.
	 */
	public Style style() {
		return Style.values()[ctx.varpbits.varpbit(43, 0, 0x3)];
	}

	/**
	 * Changes your current combat {@link Style} to the provided one.
	 *
	 * @param style The desired combat {@link Style}.
	 * @return {@code true} if the combat {@link Style} has been successfully set, {@code false} otherwise.
	 */
	public boolean style(final Style style) {
		if (style() == style) {
			return true;
		}
		if (!ctx.game.tab(Game.Tab.ATTACK)) {
			return false;
		}
		final Component c = style.comp == null ? (style.comp = ctx.widgets.component(Constants.COMBAT_OPTIONS_WIDGET, 4 + (style.ordinal() * 4))) : style.comp;
		return c != null && c.visible() && c.click() && Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return style() == style;
			}
		}, 300, 6);
	}

	/**
	 * An enumeration of possible combat styles.
	 *
	 * ACCURATE - AGGRESSIVE
	 * |              |
	 * CONTROLLED - DEFENSIVE
	 */
	public enum Style {
		ACCURATE,
		AGGRESSIVE,
		CONTROLLED,
		DEFENSIVE;
		private Component comp = null;
	}
}
