package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.INodeDeque;

public class NodeDeque extends Proxy<INodeDeque> {

	public NodeDeque(final INodeDeque wrapped) {
		super(wrapped);
	}

	public Node getSentinel() {
		if (!isNull()) {
			return new Node(get().getSentinel());
		}

		return null;
	}
}
