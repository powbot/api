package org.powerbot.bot.cache;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class JagexBufferStream {
	private static final int BYTE_SIZE = 8;
	private static final int SHORT_SIZE = 16;
	private static final int INT_SIZE = 32;
	private static final int LONG_SIZE = 64;

	public static final int NUM_LONG_BYTES = LONG_SIZE / BYTE_SIZE;

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

	private final ByteBuffer buffer;

	public JagexBufferStream(final byte[] payload) {
		this.buffer = ByteBuffer.wrap(payload);
	}

	public int getLocation() {
		return buffer.position();
	}

	public int getLength() {
		return buffer.limit();
	}

	public byte getByte() {
		return buffer.get();
	}

	public void seek(final int loc) {
		buffer.position(loc);
	}

	public final int getUByte() {
		return getByte() & BYTE_MASK;
	}

	public final int getUShort() {
		return buffer.getShort() & SHORT_MASK;
	}

	public final int getUInt24() {
		return getUByte() << BYTE_SIZE * 2 | getUByte() << BYTE_SIZE | getUByte();
	}

	private long getUInt() {
		return getInt() & INT_MASK;
	}

	public final short getShort() {
		return buffer.getShort();
	}

	public final int getInt() {
		return buffer.getInt();
	}

	public final long getLong() {
		return buffer.getLong();
	}

	public final int peek() {
		return buffer.get(buffer.position());
	}

	public final int getBigSmart() {
		return peek() >= 0 ? (this.getUShort() & 0xFFFF) : (this.getInt() & Integer.MAX_VALUE);
	}

	public int readSmartB() {
		if (peek() < 0) {
			return getInt() & Integer.MAX_VALUE;
		}
		final int val = getUShort();
		if (val > 32767) {
			return val - 65536;
		}
		return val;
	}

	public int getSmartShort() {
		return (peek() & BYTE_MASK) < 128 ? getUByte() - 64 : getUShort() - 0xc000;
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

	public byte[] getAllBytes() {
		return buffer.array();
	}

	public final byte[] getBytes(final byte[] buffer) {
		return getBytes(buffer, 0, buffer.length);
	}

	public final byte[] getBytes(final byte[] buffer, final int off, int len) {
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
