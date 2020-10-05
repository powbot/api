package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IBarComponent;

public class BarComponent extends Proxy<IBarComponent> {

	public BarComponent(final IBarComponent wrapped) {
		super(wrapped);
	}

	public int getWidth() {
		if (!isNull()) {
			return wrapped.get().getWidth();
		}

		return -1;
	}
}
