package org.powerbot.script.rt4;

import org.powerbot.bot.cache.AbstractCacheWorker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * CacheObjectConfig
 * An object holding configuration data for a GameObject within Runescape.
 */
public class CacheObjectConfig {

	public static CacheObjectConfig load(AbstractCacheWorker cacheWorker, int id) {
		return cacheWorker.objectConfigLoader().byId(id);
	}

	public interface Loader {

		CacheObjectConfig byId(int id);

	}

	public final int index;
	public String name = "null";
	public final String[] actions = new String[5];
	public int xSize = 1;
	public int[] materialPointers;
	public int ySize = 1;
	public int[] meshId;
	public int[] meshType;
	public boolean swapYZ = false;
	public int yScale = 128;
	public int xScale = 128;
	public int zScale = 128;
	public int xTranslate = 0;
	public int yTranslate = 0;
	public int stageOperationId = -1;
	public int stageIndex = -1;
	public int zTranslate = 0;
	public short[] originalColors;
	public short[] modifiedColors;
	public int unwalkableFlag;
	public boolean blocksProjectiles = true;
	public int actionsAvailable;
	public int adjustToTerrain;
	public int animationId;
	public boolean nonFlatShading;
	public boolean n;
	public int v;
	public int brightness;
	public int contrast;
	public short[] o;
	public short[] x;
	public int minimapIcon;
	public int mapSceneId;
	public boolean av;
	public boolean aa;
	public boolean solid;
	public int az;
	public int ay;
	public int as;
	public final Map<Integer, Object> params = new HashMap<>();

	public CacheObjectConfig(final int index) {
		this.index = index;
	}

	public CacheObjectConfig(int index, String name, int xSize, int[] materialPointers, int ySize, int[] meshId, int[] meshType, boolean swapYZ, int yScale, int xScale, int zScale, int xTranslate, int yTranslate, int stageOperationId, int stageIndex, int zTranslate, short[] originalColors, short[] modifiedColors, int unwalkableFlag, boolean blocksProjectiles, int actionsAvailable, int adjustToTerrain, int animationId, boolean nonFlatShading, int brightness, int contrast, int minimapIcon, int mapSceneId, boolean solid) {
		this.index = index;
		this.name = name;
		this.xSize = xSize;
		this.materialPointers = materialPointers;
		this.ySize = ySize;
		this.meshId = meshId;
		this.meshType = meshType;
		this.swapYZ = swapYZ;
		this.yScale = yScale;
		this.xScale = xScale;
		this.zScale = zScale;
		this.xTranslate = xTranslate;
		this.yTranslate = yTranslate;
		this.stageOperationId = stageOperationId;
		this.stageIndex = stageIndex;
		this.zTranslate = zTranslate;
		this.originalColors = originalColors;
		this.modifiedColors = modifiedColors;
		this.unwalkableFlag = unwalkableFlag;
		this.blocksProjectiles = blocksProjectiles;
		this.actionsAvailable = actionsAvailable;
		this.adjustToTerrain = adjustToTerrain;
		this.animationId = animationId;
		this.nonFlatShading = nonFlatShading;
		this.brightness = brightness;
		this.contrast = contrast;
		this.minimapIcon = minimapIcon;
		this.mapSceneId = mapSceneId;
		this.solid = solid;
	}

	@Override
	public String toString() {
		return "CacheObjectConfig{" +
			"index=" + index +
			", name='" + name + '\'' +
			", actions=" + Arrays.toString(actions) +
			", xSize=" + xSize +
			", materialPointers=" + Arrays.toString(materialPointers) +
			", ySize=" + ySize +
			", meshId=" + Arrays.toString(meshId) +
			", meshType=" + Arrays.toString(meshType) +
			", swapYZ=" + swapYZ +
			", yScale=" + yScale +
			", xScale=" + xScale +
			", zScale=" + zScale +
			", xTranslate=" + xTranslate +
			", yTranslate=" + yTranslate +
			", stageOperationId=" + stageOperationId +
			", stageIndex=" + stageIndex +
			", zTranslate=" + zTranslate +
			", originalColors=" + Arrays.toString(originalColors) +
			", modifiedColors=" + Arrays.toString(modifiedColors) +
			", unwalkableFlag=" + unwalkableFlag +
			", blocksProjectiles=" + blocksProjectiles +
			", actionsAvailable=" + actionsAvailable +
			", adjustToTerrain=" + adjustToTerrain +
			", animationId=" + animationId +
			", nonFlatShading=" + nonFlatShading +
			", n=" + n +
			", v=" + v +
			", brightness=" + brightness +
			", contrast=" + contrast +
			", o=" + Arrays.toString(o) +
			", x=" + Arrays.toString(x) +
			", minimapIcon=" + minimapIcon +
			", mapSceneId=" + mapSceneId +
			", av=" + av +
			", aa=" + aa +
			", solid=" + solid +
			", az=" + az +
			", ay=" + ay +
			", as=" + as +
			", params=" + params +
			'}';
	}
}
