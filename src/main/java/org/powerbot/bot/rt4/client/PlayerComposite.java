package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IPlayerComposite;

public class PlayerComposite extends Proxy<IPlayerComposite> {

	public PlayerComposite(final IPlayerComposite wrapped) {
		super(wrapped);
	}

	public int[] getAppearance() {
		if (!isNull()) {
			return wrapped.get().getAppearance();
		}

		return null;
	}
}
