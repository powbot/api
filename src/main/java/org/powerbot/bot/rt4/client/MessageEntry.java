package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IEntry;
import org.powerbot.bot.rt4.client.internal.IMessageEntry;

public class MessageEntry extends Entry<IMessageEntry> {

	public MessageEntry(final IMessageEntry wrapped) {
		super(wrapped);
	}

	public String getSender() {
		if (!isNull()) {
			return wrapped.get().getSender();
		}

		return null;
	}

	public String getMessage() {
		if (!isNull()) {
			return wrapped.get().getMessage();
		}

		return null;
	}

	public int getType() {
		if (!isNull()) {
			return wrapped.get().getType();
		}

		return -1;
	}
}
