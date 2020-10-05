package org.powerbot.bot.cache;

import java.nio.ByteBuffer;

public abstract class AbstractFileContainer {

	public abstract byte[] unpack();

	public abstract int getCRC();

	public abstract byte[] decode(final ByteBuffer b);
}
