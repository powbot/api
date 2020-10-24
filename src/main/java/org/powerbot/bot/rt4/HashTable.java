package org.powerbot.bot.rt4;

import org.powerbot.bot.rt4.client.internal.IHashTable;
import org.powerbot.bot.rt4.client.internal.INode;

public final class HashTable<T extends INode> {
	private final IHashTable wrapped;
	private INode current;
	private int caret;

	public HashTable(IHashTable cache) {
		this.wrapped = cache;
		caret = 0;
	}

	/**
	 * Get the first node in the bag.
	 *
	 * @return head
	 */
	public T getHead() {
		caret = 0;
		return getNext();
	}

	/**
	 * Get the next node based on the current position in the bag.
	 *
	 * @return next
	 */
	@SuppressWarnings("unchecked")
	public T getNext() {
		INode[] sentinels = wrapped.getBuckets();

		if (caret > 0 && sentinels[caret - 1] != current) {
			INode node = current;
			current = node.getNext();
			return (T) node;
		}

		while (sentinels.length > caret) {
			INode node = sentinels[caret++].getNext();
			if (sentinels[caret - 1] != node) {
				current = node.getNext();
				return (T) node;
			}
		}
		return null;
	}

	/**
	 * Get a node at a specific index in the bag
	 *
	 * @param index index of node
	 * @return node or null
	 */
	@SuppressWarnings("unchecked")
	public T getSentinel(int index) {
		if (index < getLength()) {
			return (T) wrapped.getBuckets()[index];
		}
		return null;
	}

	public T lookup(long id) {
		for (T node = getHead(); node != null; node = getNext()) {
			if (id == node.getNodeId()) {
				return node;
			}
		}
		return null;
	}

	/**
	 * The length of the array of nodes in this bag.
	 *
	 * @return array length
	 */
	public int getLength() {
		return wrapped.getBuckets().length;
	}

}

