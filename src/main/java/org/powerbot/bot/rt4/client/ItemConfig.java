package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IItemConfig;

public class ItemConfig extends Node<IItemConfig> {

	public ItemConfig(final IItemConfig wrapped) {
		super(wrapped);
	}

	public String getName() {
		if (!isNull()) {
			return get().getName();
		}

		return null;
	}

	public boolean isMembers() {
		if (!isNull()) {
			return get().isMembers();
		}

		return false;
	}

	public String[] getActions1() {
		if (!isNull()) {
			return get().getActions1();
		}

		return null;
	}

	public String[] getActions2() {
		if (!isNull()) {
			return get().getActions2();
		}

		return null;
	}
}
