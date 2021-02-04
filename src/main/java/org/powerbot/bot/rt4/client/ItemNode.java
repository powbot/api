package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IItemNode;

public class ItemNode extends Node<IItemNode> {

	public ItemNode(final IItemNode wrapped) {
		super(wrapped);
	}

	public int getItemId() {
		if (!isNull()) {
			return get().getItemId();
		}

		return -1;
	}

	public int getStackSize() {
		if (!isNull()) {
			return get().getStackSize();
		}

		return -1;
	}
}
