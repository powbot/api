package org.powerbot.bot.cache;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class ReferenceTable {
	public static class Entry {
		private int index;
		private int identifier;
		private int crc;
		private int version;
		private int childCount;
		private int[] childIndices;
		private int childIndexCount;
		private int[] childIdentifiers;
		private byte[] digest;

		public int getIndex() {
			return index;
		}

		public int getIdentifier() {
			return identifier;
		}

		public int getCRC() {
			return crc;
		}

		public int getVersion() {
			return version;
		}

		public int getChildCount() {
			return childCount;
		}

		public int[] getChildIndices() {
			return childIndices;
		}

		public int[] getChildIdentifiers() {
			return childIdentifiers;
		}

		public byte[] getDigest() {
			return digest;
		}
	}

	private int crc;
	private int version;
	private int entryCount;
	private Entry[] entries;

	public ReferenceTable(final ByteBuffer buffer) {
		((Buffer) buffer).position(5);
		entryCount = buffer.get() & 0xff;
		entries = new Entry[entryCount];

		for (int i = 0; i < entryCount; i++) {
			final Entry entry = entries[i] = new Entry();
			entry.crc = buffer.getInt();
			entry.version = buffer.getInt();
			final int files = buffer.getInt();
			final int size = buffer.getInt();
			entry.digest = new byte[64];
			buffer.get(entry.digest);
		}

		// not all of the version table is read here - there is still an RSA-encrypted
		// whirlpool hash of the data after this, used for verification purposes
	}

	public ReferenceTable(final int index, final ByteBuffer buffer, final ReferenceTable main, final AbstractFileContainer container) {
		crc = container.getCRC();
		if (main != null) {
			final Entry entry = main.getEntries()[index];
			if (entry.crc != crc) {
				throw new RuntimeException("CRC mismatch: " + index + "," + crc + "," + entry.crc);
			}
			final byte[] expected = entry.digest;
			final byte[] digest = container.decode(buffer);
			for (int i = 0; i < 64; i++) {
				if (digest[i] != expected[i]) {
					throw new RuntimeException("Digest mismatch " + index);
				}
			}
		}
		unpack(index, container.unpack(), main);
	}

	private void unpack(final int index, final byte[] data, final ReferenceTable main) {
		final ByteBuffer buffer = ByteBuffer.wrap(data);
		final int protocol = buffer.get() & 0xff;
		if (protocol >= 6) {
			version = buffer.getInt();
			if (main != null) {
				final Entry entry = main.getEntries()[index];
				if (entry.version != version) {
					throw new RuntimeException("Version mismatch " + index + "," + version + "," + entry.version);
				}
			}
		}
		final int flags = buffer.get() & 0xff;
		final boolean hasIdentifiers = (flags & 0x1) != 0;
		final boolean hasDigests = (flags & 0x2) != 0;
		final boolean flag4 = (flags & 0x4) != 0;
		final boolean flag8 = (flags & 0x8) != 0;

		if (protocol >= 7) {
			entryCount = getSmart(buffer);
		} else {
			entryCount = buffer.getShort() & 0xffff;
		}
		entries = new Entry[entryCount];

		int off = 0;
		int count = -1;
		for (int i = 0; i < entryCount; i++) {
			final Entry entry = entries[i] = new Entry();
			entry.index = off += protocol >= 7 ? getSmart(buffer) : (buffer.getShort() & 0xffff);
			if (entry.index > count) {
				count = entry.index;
			}
		}

		if (hasIdentifiers) {
			for (int i = 0; i < entryCount; i++) {
				entries[i].identifier = buffer.getInt();
			}
		} else {
			for (int i = 0; i < entryCount; i++) {
				entries[i].identifier = -1;
			}
		}

		for (int i = 0; i < entryCount; i++) {
			entries[i].crc = buffer.getInt();
		}

		if (flag8) {
			for (int i = 0; i < entryCount; i++) {
				final int v = buffer.getInt();
			}
		}

		if (hasDigests) {
			for (int i = 0; i < entryCount; i++) {
				final Entry entry = entries[i];
				entry.digest = new byte[64];
				buffer.get(entry.digest);
			}
		}

		if (flag4) {
			for (int i = 0; i < entryCount; i++) {
				final int v1 = buffer.getInt();
				final int v2 = buffer.getInt();
			}
		}

		for (int i = 0; i < entryCount; i++) {
			entries[i].version = buffer.getInt();
		}

		for (int i = 0; i < entryCount; i++) {
			entries[i].childCount = protocol >= 7 ? getSmart(buffer) : (buffer.getShort() & 0xffff);
		}

		for (int i = 0; i < entryCount; i++) {
			final Entry entry = entries[i];
			off = 0;
			final int children = entry.childCount;
			entry.childIndices = new int[children];
			count = -1;
			for (int j = 0; j < children; j++) {
				entry.childIndices[j] = off += protocol >= 7 ? getSmart(buffer) : (buffer.getShort() & 0xffff);
				if (entry.childIndices[j] > count) {
					count = entry.childIndices[j];
				}
			}
			entry.childIndexCount = count + 1;
			if (count + 1 == children) {
				entry.childIndices = null;
			}
		}

		if (hasIdentifiers) {
			for (int i = 0; i < entryCount; i++) {
				final Entry entry = entries[i];
				final int children = entry.childCount;
				entry.childIdentifiers = new int[entry.childIndexCount];
				for (int j = 0; j < entry.childIndexCount; j++) {
					entry.childIdentifiers[j] = -1;
				}
				for (int j = 0; j < children; j++) {
					final int k;
					if (entry.childIndices != null) {
						k = entry.childIndices[j];
					} else {
						k = j;
					}
					entry.childIdentifiers[k] = buffer.getInt();
				}
			}
		}
	}

	public int getVersion() {
		return version;
	}

	public int getEntryCount() {
		return entryCount;
	}

	private Entry[] getEntries() {
		return entries;
	}

	public Entry getEntry(final int index) {
		for (final Entry entry : entries) {
			if (entry.index == index) {
				return entry;
			}
		}

		return null;
	}

	public int getCRC() {
		return crc;
	}

	private static int getSmart(final ByteBuffer buffer) {
		final byte v = buffer.get(buffer.position());
		if (v >= 0) {
			return buffer.getShort() & 0xffff;
		} else {
			return buffer.getInt() & 0x7fffffff;
		}
	}
}
