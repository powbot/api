package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IWidgetNode;

public class WidgetNode extends Node<IWidgetNode> {

	public WidgetNode(final IWidgetNode wrapped) {
		super(wrapped);
	}

	public int getUid() {
		if (!isNull()) {
			return wrapped.get().getUid();
		}

		return -1;
	}
}
