package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IStringRecord;

public class StringRecord extends Proxy<IStringRecord> {

	public StringRecord(final IStringRecord wrapped) {
		super(wrapped);
	}

	public String getValue() {
		if (!isNull()) {
			return get().getValue();
		}

		return null;
	}
}
