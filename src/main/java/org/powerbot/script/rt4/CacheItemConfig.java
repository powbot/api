package org.powerbot.script.rt4;

import org.powerbot.bot.cache.*;
import org.powerbot.script.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * CacheItemConfig
 * An object holding configuration data for an Item within Runescape.
 */
public class CacheItemConfig implements Validatable {
	public final int index;
	private final AbstractCacheWorker worker;
	private final JagexBufferStream stream;
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
	public int[] originalColors = new int[0];
	public int[] modifiedColors = new int[0];
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
	public final Map<Integer, Object> params = new HashMap<>();
	private static final int[] EQUIP_ACTION_PARAMS = {451, 452, 453, 454, 455};


	private CacheItemConfig(final AbstractCacheWorker worker, final Block.Sector sector, final int index) {
		this.index = index;
		this.worker = worker;
		this.stream = new JagexBufferStream(sector.getPayload());
		read();
		inherit(this);
	}

	private CacheItemConfig() {
		this.index = -1;
		this.worker = null;
		this.stream = null;
	}

	public static CacheItemConfig load(final AbstractCacheWorker worker, final int id) {
		final Block b = worker.getBlock(2, 10);
		if (b == null) {
			return new CacheItemConfig();
		}
		final Block.Sector s = b.getSector(id);
		if (s == null) {
			return new CacheItemConfig();
		}
		return new CacheItemConfig(worker, s, id);
	}

	private void read() {
		int opcode;
		while ((opcode = stream.getUByte()) != 0) {
			if (opcode == 1) {
				modelId = stream.getUShort();
			} else if (opcode == 2) {
				name = stream.getString();
			} else if (opcode == 4) {
				modelZoom = stream.getUShort();
			} else if (opcode == 5) {
				modelRotationX = stream.getUShort();
			} else if (opcode == 6) {
				modelRotationY = stream.getUShort();
			} else if (opcode == 7) {
				modelOffsetX = stream.getUShort();
				if (modelOffsetX > 32767) {
					modelOffsetX -= 65536;
				}
			} else if (opcode == 8) {
				modelOffsetY = stream.getUShort();
				if (modelOffsetY > 32767) {
					modelOffsetY -= 65536;
				}
			} else if (opcode == 11) {
				stackable = true;
			} else if (opcode == 12) {
				value = stream.getInt();
			} else if (opcode == 16) {
				members = true;
			} else if (opcode == 23) {
				stream.getUShort();
				stream.getUByte();
			} else if (opcode == 24) {
				final int i = stream.getUShort();
			} else if (opcode == 25) {
				stream.getUShort();
				stream.getUByte();
			} else if (opcode == 26) {
				final int i = stream.getUShort();
			} else if (opcode >= 30 && opcode < 35) {
				groundActions[opcode - 30] = stream.getString();
				if (groundActions[opcode - 30].equalsIgnoreCase("Hidden")) {
					groundActions[opcode - 30] = null;
				}
			} else if (opcode >= 35 && opcode < 40) {
				actions[opcode - 35] = stream.getString();
			} else if (opcode == 40) {
				final int size = stream.getUByte();
				originalColors = new int[size];
				modifiedColors = new int[size];
				for (int u = 0; u < size; u++) {
					originalColors[u] = stream.getUShort();
					modifiedColors[u] = stream.getUShort();
				}
			} else if (opcode == 41) {
				final int size = stream.getUByte();
				for (int o = 0; o < size; o++) {
					stream.getUShort();
					stream.getUShort();
				}
			} else if (opcode == 42) {
				this.shiftActionIndex = stream.getByte();
			} else if (opcode == 65) {
				tradeable = true;
			} else if (opcode == 78) {
				final int i = stream.getUShort();
			} else if (opcode == 79) {
				final int i = stream.getUShort();
			} else if (opcode == 90) {
				final int i = stream.getUShort();
			} else if (opcode == 91) {
				final int i = stream.getUShort();
			} else if (opcode == 92) {
				final int i = stream.getUShort();
			} else if (opcode == 93) {
				final int i = stream.getUShort();
			} else if (opcode == 95) {
				modelRotationZ = stream.getUShort();
			} else if (opcode == 97) {
				certId = stream.getUShort();
			} else if (opcode == 98) {
				certTemplateId = stream.getUShort();
			} else if (opcode >= 100 && opcode < 110) {
				// array index k-100
				stackId = stream.getUShort();
				stackAmount = stream.getUShort();
			} else if (opcode >= 110 && opcode <= 112) {
				final float resize = stream.getUShort();
			} else if (opcode == 113) {
				final byte b = stream.getByte();
			} else if (opcode == 114) {
				final int b = stream.getByte();
			} else if (opcode == 115) {
				team = stream.getUByte();
			} else if (opcode == 139) {
				cosmeticId = stream.getUShort();
			} else if (opcode == 140) {
				cosmeticTemplateId = stream.getUShort();
			} else if (opcode == 148) {
				stream.getUShort();
			} else if (opcode == 149) {
				stream.getUShort();
			} else if (opcode == 249) {
				final int h = stream.getUByte();
				for (int m = 0; m < h; m++) {
					final boolean r = stream.getUByte() == 1;
					final int key = stream.getUInt24();
					final Object value = r ? stream.getString() : stream.getInt();
					params.put(key, value);
				}
				loadEquipActions(params);
			} else {
				Logger.getLogger(getClass().getSimpleName()).info("unknown opcode " + opcode);
				break;
			}
		}
	}

	private void inherit(final CacheItemConfig item) {
		if (item.certTemplateId != -1) {
			inheritCert(item);
		}
		if (item.cosmeticTemplateId != -1) {
			inheritCosmetic(item);
		}
		if (item.shiftActionIndex >= 0 && item.shiftActionIndex < item.actions.length) {
			item.shiftAction = item.actions[shiftActionIndex];
		}
	}

	private void delegate(final CacheItemConfig item, final int sourceId) {
		final CacheItemConfig source = load(worker, sourceId);
		if (!source.valid()) {
			return;
		}
		item.groundActions = source.groundActions;
		item.actions = source.actions;
		item.name = source.name;
		item.members = source.members;
		item.value = 0;
		item.team = source.team;
		item.actions[4] = "Discard";
	}

	private void inheritCert(final CacheItemConfig item) {
		final CacheItemConfig note = load(worker, item.certId);
		if (!note.valid()) {
			return;
		}
		item.value = note.value;
		item.name = note.name;
		item.stackable = true;
		item.members = note.members;
		item.noted = true;
	}

	private void inheritCosmetic(final CacheItemConfig item) {
		delegate(item, item.cosmeticId);
		item.cosmetic = true;
	}

	private void loadEquipActions(final Map<Integer, Object> params) {
		int count = 0, idx = 0;
		for (final int id : EQUIP_ACTION_PARAMS) {
			if (params.containsKey(id)) {
				count++;
			}
		}
		if (count == 0) {
			return;
		}
		this.equipActions = new String[count];
		for (final int id : EQUIP_ACTION_PARAMS) {
			final String action = (String) params.get(id);
			if (action != null) {
				equipActions[idx++] = action;
			}
		}
	}

	@Override
	public boolean valid() {
		return index > -1 && worker != null && stream != null;
	}
}
