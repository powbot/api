package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.ILinkedList;
import org.powerbot.bot.rt4.client.internal.INode;

public class LinkedList<T> extends Proxy<ILinkedList> {

	public LinkedList(final ILinkedList wrapped) {
		super(wrapped);
	}

	public Node getSentinel() {
		if (!isNull()) {
			return new Node(wrapped.get().getSentinel());
		}

		return null;
	}
}
