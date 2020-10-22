package org.powerbot.script.rt4;

import org.powerbot.bot.cache.AbstractCacheWorker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * CacheNpcConfig
 * An object holding configuration data for a Npc within Runescape.
 */
public class CacheNpcConfig {

	public interface Loader {

		CacheNpcConfig byId(int id);

	}

	public static CacheNpcConfig load(AbstractCacheWorker worker, int id) {
		return worker.npcConfigLoader().byId(id);
	}

	public final int index;
	public String name = "null";
	public int[] modelIds, materialPointers, additionalModels;
	public int[][] modelOffsets;
	public int size = 552360651;
	public int idleSequence = -1;
	public int turnLeftSequence = -1;
	public int turnRightSequence = -1;
	public int walkSequence = -1;
	public int walkBackSequence = -1;
	public int walkLeftSequence = -1;
	public int walkRightSequence = -1;
	public final String[] actions = new String[5];
	public boolean visible = true;
	public int level = -1;
	public int widthScale = 128;
	public int heightScale = 128;
	public boolean a = false;
	public int lightModifier = 0;
	public int shadowModifier = 0;
	public int headIconPrayer = -1;
	public int turnDegrees = -1;
	public int stageOperation = -1;
	public int stageIndex = -1;
	public boolean clickable = true;
	public boolean au = true;
	public boolean ao = false;
	public int af = -1;
	public short[] recolorOriginal, recolorTarget, q, g;
	public final Map<Integer, Object> params = new HashMap<>();

	public CacheNpcConfig(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "CacheNpcConfig{" +
			"index=" + index +
			", name='" + name + '\'' +
			", modelIds=" + Arrays.toString(modelIds) +
			", materialPointers=" + Arrays.toString(materialPointers) +
			", additionalModels=" + Arrays.toString(additionalModels) +
			", modelOffsets=" + Arrays.toString(modelOffsets) +
			", size=" + size +
			", idleSequence=" + idleSequence +
			", turnLeftSequence=" + turnLeftSequence +
			", turnRightSequence=" + turnRightSequence +
			", walkSequence=" + walkSequence +
			", walkBackSequence=" + walkBackSequence +
			", walkLeftSequence=" + walkLeftSequence +
			", walkRightSequence=" + walkRightSequence +
			", actions=" + Arrays.toString(actions) +
			", visible=" + visible +
			", level=" + level +
			", widthScale=" + widthScale +
			", heightScale=" + heightScale +
			", a=" + a +
			", lightModifier=" + lightModifier +
			", shadowModifier=" + shadowModifier +
			", headIconPrayer=" + headIconPrayer +
			", turnDegrees=" + turnDegrees +
			", stageOperation=" + stageOperation +
			", stageIndex=" + stageIndex +
			", clickable=" + clickable +
			", au=" + au +
			", ao=" + ao +
			", af=" + af +
			", recolorOriginal=" + Arrays.toString(recolorOriginal) +
			", recolorTarget=" + Arrays.toString(recolorTarget) +
			", q=" + Arrays.toString(q) +
			", g=" + Arrays.toString(g) +
			", params=" + params +
			'}';
	}
}
