package org.powerbot.script.rt4;

import org.powerbot.bot.cache.AbstractCacheWorker;
import org.powerbot.script.Validatable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * CacheItemConfig
 * An object holding configuration data for an Item within RuneScape.
 */
public class CacheItemConfig implements Validatable {

	public interface Loader {

		CacheItemConfig byId(int id);

	}

	public static CacheItemConfig load(AbstractCacheWorker worker, int id) {
		return worker.itemConfigLoader().byId(id);
	}

	public final int index;
	public String name = "";
	public String shiftAction = "";
	public boolean tradeable;
	public boolean stackable;
	public boolean members;
	public int team = -1;
	public int value = -1;
	public int modelId = -1;
	public int modelZoom = 0;
	public int modelOffsetX = 0;
	public int modelOffsetY = 0;
	public int modelRotationX = 0;
	public int modelRotationY = 0;
	public int modelRotationZ = 0;
	public short[] originalColors = new short[0];
	public short[] modifiedColors = new short[0];
	public int cosmeticTemplateId = -1;
	public int cosmeticId = -1;
	public int certTemplateId = -1;
	public int certId = -1;
	public int stackId = -1;
	public int stackAmount = -1;
	public int shiftActionIndex = -1;
	public boolean cosmetic, noted;
	public String[] actions = {null, null, null, null, "Drop"};
	public String[] groundActions = {null, null, "Take", null, null};
	public String[] equipActions = new String[0];
	public int equippedModelMale1;
	public int equippedModelMaleOffsetY;
	public int equippedModelMale2;
	public int equippedModelMale3;
	public int equippedModelMaleDialogue1;
	public int equippedModelMaleDialogue2;
	public int equippedModelFemale1;
	public int equippedModelFemaleOffsetY;
	public int equippedModelFemale2;
	public int equippedModelFemale3;
	public int equippedModelFemaleDialogue1;
	public int equippedModelFemaleDialogue2;
	public short[] originalModelTexture = new short[0];
	public short[] modifiedModelTexture = new short[0];
	public int modelScaleX;
	public int modelScaleY;
	public int modelScaleZ;
	public byte lightIntensity;
	public byte lightMag;
	public int placeholderId;
	public int placeholderTemplateId;

	public final Map<Integer, Object> params = new HashMap<>();

	public CacheItemConfig(final int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "CacheItemConfig{" +
			"index=" + index +
			", name='" + name + '\'' +
			", shiftAction='" + shiftAction + '\'' +
			", tradeable=" + tradeable +
			", stackable=" + stackable +
			", members=" + members +
			", team=" + team +
			", value=" + value +
			", modelId=" + modelId +
			", modelZoom=" + modelZoom +
			", modelOffsetX=" + modelOffsetX +
			", modelOffsetY=" + modelOffsetY +
			", modelRotationX=" + modelRotationX +
			", modelRotationY=" + modelRotationY +
			", modelRotationZ=" + modelRotationZ +
			", originalColors=" + Arrays.toString(originalColors) +
			", modifiedColors=" + Arrays.toString(modifiedColors) +
			", cosmeticTemplateId=" + cosmeticTemplateId +
			", cosmeticId=" + cosmeticId +
			", certTemplateId=" + certTemplateId +
			", certId=" + certId +
			", stackId=" + stackId +
			", stackAmount=" + stackAmount +
			", shiftActionIndex=" + shiftActionIndex +
			", cosmetic=" + cosmetic +
			", noted=" + noted +
			", actions=" + Arrays.toString(actions) +
			", groundActions=" + Arrays.toString(groundActions) +
			", equipActions=" + Arrays.toString(equipActions) +
			", equippedModelMale1=" + equippedModelMale1 +
			", equippedModelMaleOffsetY=" + equippedModelMaleOffsetY +
			", equippedModelMale2=" + equippedModelMale2 +
			", equippedModelMale3=" + equippedModelMale3 +
			", equippedModelMaleDialogue1=" + equippedModelMaleDialogue1 +
			", equippedModelMaleDialogue2=" + equippedModelMaleDialogue2 +
			", equippedModelFemale1=" + equippedModelFemale1 +
			", equippedModelFemaleOffsetY=" + equippedModelFemaleOffsetY +
			", equippedModelFemale2=" + equippedModelFemale2 +
			", equippedModelFemale3=" + equippedModelFemale3 +
			", equippedModelFemaleDialogue1=" + equippedModelFemaleDialogue1 +
			", equippedModelFemaleDialogue2=" + equippedModelFemaleDialogue2 +
			", originalModelTexture=" + Arrays.toString(originalModelTexture) +
			", modifiedModelTexture=" + Arrays.toString(modifiedModelTexture) +
			", modelScaleX=" + modelScaleX +
			", modelScaleY=" + modelScaleY +
			", modelScaleZ=" + modelScaleZ +
			", lightIntensity=" + lightIntensity +
			", lightMag=" + lightMag +
			", placeholderId=" + placeholderId +
			", placeholderTemplateId=" + placeholderTemplateId +
			", params=" + params +
			'}';
	}

	@Override
	public boolean valid() {
		return index > 0;
	}
}
