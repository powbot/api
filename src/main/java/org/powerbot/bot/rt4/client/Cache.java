package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.ICache;

public class Cache extends Proxy<ICache> {

	public Cache(final ICache wrapped) {
		super(wrapped);
	}

	public IterableHashTable getTable() {
		if (!isNull()) {
			return new IterableHashTable(wrapped.get().getTable());
		}

		return null;
	}
}
