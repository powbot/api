package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.client.*;

/**
 * Skills
 */
public class Skills extends ClientAccessor {
	public Skills(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * Returns an array of experience required to complete levels in runecape.
	 * The index is the level and the value stored at the index is the experience to complete that level.
	 * Example: exps_at()[1] would be 83.
	 *
	 * @return the current level at the specified index
	 */
	public int[] exps_at() {
		int points = 0;
		final int[] exp = new int[100];
		for (int lvl = 1; lvl < 100; lvl++) {
			points += Math.floor(lvl + 300d * Math.pow(2, lvl / 7d));
			exp[lvl] = (int) Math.floor(points / 4d);
		}
		return exp;
	}

	/**
	 * Returns the level of a skill at a given index without boosts/reductions.
	 * The index is to be obtained via {@link org.powerbot.script.rt4}.
	 * Example: realLevel(Constants.SKILLS_STRENGTH) would return 70, if my strenght level is currently 50/70.
	 *
	 * @param index the index of the skill from {@link org.powerbot.script.rt4.Constants}
	 * @return the current level at the specified index
	 */
	public int realLevel(final int index) {
		final int[] levels = realLevels();
		if (index >= 0 && index < levels.length) {
			return levels[index];
		}
		return -1;
	}

	/**
	 * Returns the level of a skill at a given index with boosts/reductions.
	 * The index is to be obtained via {@link org.powerbot.script.rt4}.
	 * Example: level(Constants.SKILLS_STRENGTH) would return 50, if my strenght level is currently 50/70.
	 *
	 * @param index the index of the skill from {@link org.powerbot.script.rt4.Constants}
	 * @return the current level at the specified index
	 */
	public int level(final int index) {
		final int[] levels = levels();
		if (index >= 0 && index < levels.length) {
			return levels[index];
		}
		return -1;
	}

	/**
	 * Returns the experience of the skill at the provided index.
	 * The index is to be obtained via {@link org.powerbot.script.rt4.Constants}.
	 *
	 * @param index the index of the skill from {@link org.powerbot.script.rt4.Constants}
	 * @return the experience at the specified index
	 */
	public int experience(final int index) {
		final int[] exps = experiences();
		if (index >= 0 && index < exps.length) {
			return exps[index];
		}
		return -1;
	}

	/**
	 * Returns an array of levels of skills which do not consider boosts/reductions.
	 * The indexes are to be obtained via {@link org.powerbot.script.rt4}.
	 *
	 * @return the experience at the specified index
	 */
	public int[] realLevels() {
		final Client c = ctx.client();
		final int[] arr = c != null ? c.getSkillLevels2() : new int[0];
		return arr != null ? arr : new int[0];
	}

	/**
	 * Returns an array of levels of skills which do consider boosts/reductions.
	 * The indexes are to be obtained via {@link org.powerbot.script.rt4}.
	 *
	 * @return the experience at the specified index
	 */
	public int[] levels() {
		final Client c = ctx.client();
		final int[] arr = c != null ? c.getSkillLevels1() : new int[0];
		return arr != null ? arr : new int[0];
	}

	/**
	 * Returns an array of current experience of skills which do consider boosts/reductions.
	 * The indexes are to be obtained via {@link org.powerbot.script.rt4.Constants}.
	 *
	 * @return the experience at the specified index
	 */
	public int[] experiences() {
		final Client c = ctx.client();
		final int[] arr = c != null ? c.getSkillExps() : new int[0];
		return arr != null ? arr : new int[0];
	}

	/**
	 * Determines the level at the specified amount of exp.
	 *
	 * @param exp the exp to convert to level
	 * @return the level with the given amount of exp
	 */
	public int levelAt(final int exp) {
		for (int i = Constants.SKILLS_XP.length - 1; i > 0; i--) {
			if (exp > Constants.SKILLS_XP[i]) {
				return i;
			}
		}
		return 1;
	}

	/**
	 * Determines the experience required for the specified level.
	 *
	 * @param level the level to get the exp at
	 * @return the exp at the specified level
	 */
	public int experienceAt(final int level) {
		if (level < 0 || level > 120) {
			return -1;
		}
		return Constants.SKILLS_XP[level];
	}
}
