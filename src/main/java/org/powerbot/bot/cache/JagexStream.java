package org.powerbot.bot.cache;

import java.nio.BufferUnderflowException;

/**
 * Read cache
 * @deprecated
 * This class is deprecated.
 * <p> Use {@link JagexBufferStream} instead.
 */
@Deprecated
public class JagexStream {
	private static final int BYTE_SIZE = 8;
	private static final int SHORT_SIZE = 16;
	private static final int INT_SIZE = 32;
	private static final int LONG_SIZE = 64;

	public static final int NUM_LONG_BYTES = LONG_SIZE / BYTE_SIZE;
	private static final int NUM_INT_BYTES = INT_SIZE / BYTE_SIZE;
	private static final int NUM_SHORT_BYTES = SHORT_SIZE / BYTE_SIZE;

	private static final int[] MASKS = new int[INT_SIZE];

	static {
		for (int i = 1, currentMask = 0; i < MASKS.length; ++i) {
			currentMask = currentMask << 1 | 1;
			MASKS[i] = currentMask;
		}
	}

	private static final int BYTE_SIGN_BIT = 1 << (BYTE_SIZE - 1);
	private static final int BYTE_MASK = MASKS[BYTE_SIZE];
	public static final int SKIP_SIGN_BYTE_MASK = MASKS[BYTE_SIZE - 1];

	public static final int SHORT_SIGN_BIT = 1 << (SHORT_SIZE - 1);
	public static final int SHORT_MASK = MASKS[SHORT_SIZE];
	public static final int SKIP_SIGN_SHORT_MASK = MASKS[SHORT_SIZE - 1];

	public static final int INT_SIGN_BIT = 1 << (INT_SIZE - 1);
	public static final int SKIP_SIGN_INT_MASK = MASKS[INT_SIZE - 1];
	private static final long INT_MASK = 0xffffffffL;

	private static final int MAX_NOSIGN_SHORT = 0x7fff;

	private static final char[] charSubs = {'\u20AC', '\0', '\u201A', '\u0192', '\u201E', '\u2026', '\u2020',
			'\u2021', '\u02C6', '\u2030', '\u0160', '\u2039', '\u0152', '\0', '\u017D', '\0', '\0', '\u2018',
			'\u2019', '\u201C', '\u201D', '\u2022', '\u2013', '\u2014', '\u02DC', '\u2122', '\u0161', '\u203A',
			'\u0153', '\0', '\u017E', '\u0178'};

	private final byte[] payload;
	private int offset;

	public JagexStream(final byte[] payload) {
		this.payload = payload;
		offset = 0;
	}

	public int getLocation() {
		return offset;
	}

	public int getLength() {
		return payload.length;
	}

	public byte getByte() {
		assertLeft(1);
		return payload[offset++];
	}

	public void seek(final int loc) {
		offset = loc;
	}

	private int getLeft() {
		return getLength() - getLocation();
	}

	private void assertLeft(final int num) {
		if (!checkLeft(num)) {
			throw new BufferUnderflowException();
		}
	}

	private boolean checkLeft(final int num) {
		final int byteLeft = getLeft();
		return byteLeft >= num;
	}

	public final int getUByte() {
		return getByte() & BYTE_MASK;
	}

	public final int getUShort() {
		return getUByte() << BYTE_SIZE | getUByte();
	}

	public final int getUInt24() {
		return getUByte() << BYTE_SIZE * 2 | getUByte() << BYTE_SIZE | getUByte();
	}

	private long getUInt() {
		return getInt() & INT_MASK;
	}

	public final short getShort() {
		return (short) getUShort();
	}

	public final int getInt() {
		return getUByte() << BYTE_SIZE * 3 | getUByte() << BYTE_SIZE * 2 | getUByte() << BYTE_SIZE | getUByte();
	}

	public final long getLong() {
		return getUInt() << INT_SIZE | getUInt();
	}

	public final int getReferenceTableSmart() {
		return getSmart(NUM_SHORT_BYTES, NUM_INT_BYTES, false);
	}

	public final int getBigSmart() {
		final int first = getUByte();
		int joined;
		if ((first & BYTE_SIGN_BIT) != BYTE_SIGN_BIT) {
			joined = joinSmart(first, NUM_SHORT_BYTES, false);
			if (joined == MAX_NOSIGN_SHORT) {
				joined = -1;
			}
		} else {
			joined = joinSmart(first, NUM_INT_BYTES, false);
		}
		return joined;
	}

	public int readSmartB() {
		final int val = getShort() & 0xFFFF;
		if (val > 32767) {
			return val - 65536;
		}
		return val;
	}

	public final int getSmart() {
		return getSmart(1, NUM_SHORT_BYTES, false);
	}

	public final int getSignedSmart() {
		return getSmart(1, NUM_SHORT_BYTES, true);
	}

	public int getSmartMinusOne() {
		return getSmart() - 1;
	}

	public int getSmartShort() {
		final int val = getShort() & 0xFFFF;
		if (val > 32767) {
			return val - 65536;
		}
		return val;
	}

	public final int getSmarts() {
		int i = 0;
		for (int j = MAX_NOSIGN_SHORT; j == MAX_NOSIGN_SHORT; i += j) {
			j = getSmart();
		}
		return i;
	}

	private int getSmart(final int smallSize, final int bigSize, final boolean signed) {
		final int num = getUByte();
		final int size = ((num & BYTE_SIGN_BIT) == BYTE_SIGN_BIT ? bigSize : smallSize);
		return joinSmart(num, size, signed);
	}

	private int joinSmart(final int first, final int size, final boolean signed) {
		int num = first & ~BYTE_SIGN_BIT;
		for (int i = 1; i < size; ++i) {
			num = (num << BYTE_SIZE) | getUByte();
		}
		if (signed) {
			num -= (1 << size * BYTE_SIZE - 2);
		}
		return num;
	}

	protected final int joinSmart(final int size, final boolean signed) {
		return getSmart(getUByte(), size, signed);
	}

	public byte[] getAllBytes() {
		return payload;
	}

	public final byte[] getBytes(final byte[] buffer) {
		return getBytes(buffer, 0, buffer.length);
	}

	private byte[] getBytes(final byte[] buffer, final int off, int len) {
		assertLeft(len);
		len += off;
		for (int k = off; k < len; k++) {
			buffer[k] = getByte();
		}
		return buffer;
	}

	public final String getString() {
		final StringBuilder ret = new StringBuilder();
		int c;
		while ((c = getUByte()) != 0) {
			if (c >= 128 && c < 160) {
				char n = charSubs[c - 128];
				if (n == 0) {
					n = '?';
				}
				c = n;
			}
			ret.append((char) c);
		}
		return ret.toString();
	}

	public final String getJagString() {
		if (getUByte() != 0) {
			throw new RuntimeException();
		}
		return getString();
	}

	public final void reset() {
		seek(0);
	}
}
