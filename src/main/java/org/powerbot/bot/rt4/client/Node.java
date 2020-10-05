package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.INode;

public class Node<T extends INode> extends Proxy<T> {

	public Node(final T wrapped) {
		super(wrapped);
	}

	public Node getNext() {
		if (!isNull()) {
			return new Node(wrapped.get().getNext());
		}

		return null;
	}

	public long getNodeId() {
		if (!isNull()) {
			return wrapped.get().getNodeId();
		}

		return -1L;
	}

	/**
	 * @deprecated Use Node#getNodeId
	 */
	public long getId() {
		if (!isNull()) {
			return wrapped.get().getNodeId();
		}

		return -1L;
	}
}
