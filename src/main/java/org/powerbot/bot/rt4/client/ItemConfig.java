package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IItemConfig;

public class ItemConfig extends Node<IItemConfig> {

	public ItemConfig(final IItemConfig wrapped) {
		super(wrapped);
	}

	public String getName() {
		if (!isNull()) {
			return wrapped.get().getName();
		}

		return null;
	}

	public boolean isMembers() {
		if (!isNull()) {
			return wrapped.get().isMembers();
		}

		return false;
	}

	public String[] getActions1() {
		if (!isNull()) {
			return wrapped.get().getActions1();
		}

		return null;
	}

	public String[] getActions2() {
		if (!isNull()) {
			return wrapped.get().getActions2();
		}

		return null;
	}
}
