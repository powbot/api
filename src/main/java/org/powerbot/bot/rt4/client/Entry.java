package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IEntry;

public class Entry<T extends IEntry> extends Node<T> {

	public Entry(final T wrapped) {
		super(wrapped);
	}

	public Entry getNext() {
		if (!isNull()) {
			return new Entry(get().getNext());
		}

		return null;
	}
}
