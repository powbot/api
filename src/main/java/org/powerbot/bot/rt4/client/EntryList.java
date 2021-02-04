package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IEntryList;

public class EntryList extends Proxy<IEntryList> {

	public EntryList(final IEntryList wrapped) {
		super(wrapped);
	}

	public Entry getSentinel() {
		if (!isNull()) {
			return new Entry(get().getSentinel());
		}

		return null;
	}
}
