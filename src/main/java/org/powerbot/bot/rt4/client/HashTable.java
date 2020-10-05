package org.powerbot.bot.rt4.client;

import org.powerbot.bot.Proxy;
import org.powerbot.bot.rt4.client.internal.IHashTable;
import org.powerbot.bot.rt4.client.internal.INode;

public class HashTable extends Proxy<IHashTable> {

	public HashTable(final IHashTable wrapped) {
		super(wrapped);
	}

	public Node[] getBuckets() {
		if (!isNull()) {
			final INode[] nodes = wrapped.get().getBuckets();
			final Node[] buckets = nodes != null ? new Node[nodes.length] : null;
			if (nodes != null) {
				for (int i = 0; i < nodes.length; i++) {
					buckets[i] = new Node(nodes[i]);
				}
			}

			return buckets;
		}

		return null;
	}

	public int getSize() {
		if (!isNull()) {
			return wrapped.get().getSize();
		}

		return -1;
	}
}
