package org.powerbot.script.rt4;

import org.powerbot.bot.cache.*;

import java.util.HashMap;
import java.util.Map;

/**
 * CacheNpcConfig
 * An object holding configuration data for a Npc within Runescape.
 */
public class CacheNpcConfig {
	public final int index;
	private final JagexBufferStream stream;
	public String name = "null";
	public int[] modelIds, materialPointers, d;
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
	public int ag = 0;
	public int am = 0;
	public int headIconPrayer = -1;
	public int az = -1;
	public int stageOperation = -1;
	public int stageIndex = -1;
	public boolean clickable = true;
	public boolean au = true;
	public boolean ao = false;
	public int af = -1;
	public short[] colors1, colors2, q, g;
	public final Map<Integer, Object> params = new HashMap<>();

	public CacheNpcConfig(final Block.Sector sector, final int index) {
		this.index = index;
		stream = new JagexBufferStream(sector.getPayload());
		read();
	}

	public static CacheNpcConfig load(final AbstractCacheWorker worker, final int id) {
		final Block b = worker.getBlock(2, 9);
		if (b == null) {
			return null;
		}
		final Block.Sector s = b.getSector(id);
		if (s == null) {
			return null;
		}
		return new CacheNpcConfig(s, id);
	}

	private void read() {
		int opcode;
		while ((opcode = stream.getUByte()) != 0) {
			switch (opcode) {
				case 1: {
					final int len = stream.getUByte();
					this.modelIds = new int[len];
					for (int index = 0; index < len; ++index) {
						this.modelIds[index] = stream.getUShort();
					}
					break;
				}
				case 2:
					this.name = stream.getString();
					break;
				case 12:
					this.size = stream.getUByte();
					break;
				case 13:
					this.idleSequence = stream.getUShort();
					break;
				case 14:
					this.walkSequence = stream.getUShort();
					break;
				case 15:
					this.turnLeftSequence = stream.getUShort();
					break;
				case 16:
					this.turnRightSequence = stream.getUShort();
					break;
				case 17:
					this.walkSequence = stream.getUShort();
					this.walkBackSequence = stream.getUShort();
					this.walkLeftSequence = stream.getUShort();
					this.walkRightSequence = stream.getUShort();
					break;
				case 30:
				case 31:
				case 32:
				case 33:
				case 34:
					this.actions[opcode - 30] = stream.getString();
					if (this.actions[opcode - 30].equalsIgnoreCase("Hidden")) {
						this.actions[opcode - 30] = null;
					}
					break;
				case 40: {
					final int len = stream.getUByte();
					this.colors1 = new short[len];
					this.colors2 = new short[len];
					for (int index = 0; index < len; ++index) {
						this.colors1[index] = (short) stream.getUShort();
						this.colors2[index] = (short) stream.getUShort();
					}
				}
				break;
				case 41: {
					final int len = stream.getUByte();
					this.q = new short[len];
					this.g = new short[len];
					for (int index = 0; index < len; ++index) {
						this.q[index] = (short) stream.getUShort();
						this.g[index] = (short) stream.getUShort();
					}
				}
				break;
				case 60: {
					final int len = stream.getUByte();
					this.d = new int[len];
					for (int index = 0; index < len; ++index) {
						this.d[index] = stream.getUShort();
					}
				}
				break;
				case 93:
					this.visible = false;
					break;
				case 95:
					this.level = stream.getUShort();
					break;
				case 97:
					this.widthScale = stream.getUShort();
					break;
				case 98:
					this.heightScale = stream.getUShort();
					break;
				case 99:
					this.a = true;
					break;
				case 100:
					this.ag = stream.getByte();
					break;
				case 101:
					this.am = stream.getByte();
					break;
				case 102:
					this.headIconPrayer = stream.getUShort();
					break;
				case 103:
					this.az = stream.getUShort();
					break;
				case 106:
				case 118:
					this.stageOperation = stream.getUShort();
					if (this.stageOperation == '\uffff') {
						this.stageOperation = -1;
					}
					this.stageIndex = stream.getUShort();
					if (this.stageIndex == '\uffff') {
						this.stageIndex = -1;
					}
					int defaultMaterialPointer = -1;
					if (opcode == 118) {
						defaultMaterialPointer = stream.getUShort();
						if (defaultMaterialPointer == '\uffff') {
							defaultMaterialPointer = -1;
						}
					}
					final int count = stream.getUByte();
					this.materialPointers = new int[count + 2];
					for (int index = 0; index <= count; ++index) {
						this.materialPointers[index] = stream.getUShort();
						if (this.materialPointers[index] == '\uffff') {
							this.materialPointers[index] = -1;
						}
					}
					this.materialPointers[count + 1] = defaultMaterialPointer;
					break;
				case 107:
					this.clickable = false;
					break;
				case 109:
					this.au = false;
					break;
				case 111:
					this.ao = true;
					break;
				case 112:
					this.af = stream.getUByte();
					break;
				case 121:
					modelOffsets = new int[modelIds.length][3];
					final int t = stream.getUByte();
					for (int i = 0; i < t; i++) {
						final int u = stream.getUByte();
						modelOffsets[u][0] = stream.getByte();
						modelOffsets[u][1] = stream.getByte();
						modelOffsets[u][2] = stream.getByte();
					}
					break;
				case 249:
					final int h = stream.getUByte();
					for (int m = 0; m < h; m++) {
						final boolean r = stream.getUByte() == 1;
						final int key = stream.getUInt24();
						final Object value = r ? stream.getString() : stream.getInt();
						params.put(key, value);
					}
					break;
			}
		}
	}
}
