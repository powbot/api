package org.powerbot.bot.cache;

import java.util.Arrays;

public class Block {
	private final ReferenceTable.Entry entry;
	private final byte[] block;
	private Sector[] sectors;

	public static class Sector {
		private final int index, identifier;
		private final byte[] payload;

		public Sector(final int index, final int identifier, final byte[] payload) {
			this.index = index;
			this.identifier = identifier;
			this.payload = payload;
		}

		public int getIndex() {
			return index;
		}

		public int getIdentifier() {
			return identifier;
		}

		public byte[] getPayload() {
			return payload;
		}
	}

	public Block(final ReferenceTable.Entry entry, final byte[] block) {
		this.entry = entry;
		this.block = block;
		init();
	}

	private void init() {
		if (entry.getChildCount() == 1) {
			sectors = new Block.Sector[]{new Block.Sector(0, -1, block)};
			return;
		}

		final JagexBufferStream stream = new JagexBufferStream(block);
		stream.seek(stream.getLength() - 1);
		final int parts = stream.getUByte();
		final int start = stream.getLength() - 1 - 4 * parts * entry.getChildCount();

		stream.seek(start);
		final byte[][] sectors = new byte[entry.getChildCount()][];
		final int[] s = new int[sectors.length];
		for (int p = 0; p < parts; p++) {
			int t = 0;
			for (int i = 0; i < s.length; i++) {
				t += stream.getInt();
				s[i] += t;
			}
		}
		for (int i = 0; i < sectors.length; i++) {
			sectors[i] = new byte[s[i]];
		}

		stream.seek(start);
		final int[] offsets = new int[entry.getChildCount()];
		for (int p = 0; p < parts; ++p) {
			int blockSize = 0;
			int sourceOffset = 0;
			for (int i = 0; i < offsets.length; ++i) {
				blockSize += stream.getInt();
				System.arraycopy(stream.getAllBytes(), sourceOffset, sectors[i], offsets[i], blockSize);
				offsets[i] += blockSize;
				sourceOffset += blockSize;
			}
		}

		this.sectors = new Sector[sectors.length];
		for (int i = 0; i < sectors.length; i++) {
			this.sectors[i] = new Sector(i, -1, sectors[i]);
		}
	}

	public byte[] getBlock() {
		return block;
	}

	public Sector[] getSectors() {
		return sectors;
	}

	public Sector getSector(final int id) {
		final int[] a = entry.getChildIndices();
		final int index = a == null ? id : Arrays.binarySearch(a, id);
		if (index < 0 || index >= sectors.length) {
			return null;
		}
		return sectors[index];
	}
}
