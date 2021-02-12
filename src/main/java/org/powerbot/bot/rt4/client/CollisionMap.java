package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.ICollisionMap;

public class CollisionMap extends Proxy<ICollisionMap> {

	public CollisionMap(final ICollisionMap wrapped) {
		super(wrapped);
	}

	public int[][] getFlags() {
		if (!isNull()) {
			return get().getFlags();
		}

		return null;
	}

	public int getOffsetX() {
		if (!isNull()) {
			return get().getOffsetX();
		}

		return -1;
	}

	public int getOffsetY() {
		if (!isNull()) {
			return get().getOffsetY();
		}

		return -1;
	}
}
