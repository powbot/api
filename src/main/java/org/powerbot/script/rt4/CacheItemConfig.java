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

	public CacheItemConfig(int index, String name, String shiftAction, boolean tradeable, boolean stackable, boolean members, int team, int value, int modelId, int modelZoom, int modelOffsetX, int modelOffsetY, int modelRotationX, int modelRotationY, int modelRotationZ, short[] originalColors, short[] modifiedColors, int cosmeticTemplateId, int cosmeticId, int certTemplateId, int certId, int stackId, int stackAmount, int shiftActionIndex, boolean cosmetic, boolean noted, String[] actions, String[] groundActions, String[] equipActions, int equippedModelMale1, int equippedModelMaleOffsetY, int equippedModelMale2, int equippedModelMale3, int equippedModelMaleDialogue1, int equippedModelMaleDialogue2, int equippedModelFemale1, int equippedModelFemaleOffsetY, int equippedModelFemale2, int equippedModelFemale3, int equippedModelFemaleDialogue1, int equippedModelFemaleDialogue2, short[] originalModelTexture, short[] modifiedModelTexture, int modelScaleX, int modelScaleY, int modelScaleZ, byte lightIntensity, byte lightMag, int placeholderId, int placeholderTemplateId) {
		this.index = index;
		this.name = name;
		this.shiftAction = shiftAction;
		this.tradeable = tradeable;
		this.stackable = stackable;
		this.members = members;
		this.team = team;
		this.value = value;
		this.modelId = modelId;
		this.modelZoom = modelZoom;
		this.modelOffsetX = modelOffsetX;
		this.modelOffsetY = modelOffsetY;
		this.modelRotationX = modelRotationX;
		this.modelRotationY = modelRotationY;
		this.modelRotationZ = modelRotationZ;
		this.originalColors = originalColors;
		this.modifiedColors = modifiedColors;
		this.cosmeticTemplateId = cosmeticTemplateId;
		this.cosmeticId = cosmeticId;
		this.certTemplateId = certTemplateId;
		this.certId = certId;
		this.stackId = stackId;
		this.stackAmount = stackAmount;
		this.shiftActionIndex = shiftActionIndex;
		this.cosmetic = cosmetic;
		this.noted = noted;
		this.actions = actions;
		this.groundActions = groundActions;
		this.equipActions = equipActions;
		this.equippedModelMale1 = equippedModelMale1;
		this.equippedModelMaleOffsetY = equippedModelMaleOffsetY;
		this.equippedModelMale2 = equippedModelMale2;
		this.equippedModelMale3 = equippedModelMale3;
		this.equippedModelMaleDialogue1 = equippedModelMaleDialogue1;
		this.equippedModelMaleDialogue2 = equippedModelMaleDialogue2;
		this.equippedModelFemale1 = equippedModelFemale1;
		this.equippedModelFemaleOffsetY = equippedModelFemaleOffsetY;
		this.equippedModelFemale2 = equippedModelFemale2;
		this.equippedModelFemale3 = equippedModelFemale3;
		this.equippedModelFemaleDialogue1 = equippedModelFemaleDialogue1;
		this.equippedModelFemaleDialogue2 = equippedModelFemaleDialogue2;
		this.originalModelTexture = originalModelTexture;
		this.modifiedModelTexture = modifiedModelTexture;
		this.modelScaleX = modelScaleX;
		this.modelScaleY = modelScaleY;
		this.modelScaleZ = modelScaleZ;
		this.lightIntensity = lightIntensity;
		this.lightMag = lightMag;
		this.placeholderId = placeholderId;
		this.placeholderTemplateId = placeholderTemplateId;
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
