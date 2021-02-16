package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IPlayerComposite;

import java.lang.reflect.Field;

public class PlayerComposite extends Proxy<IPlayerComposite> {

	public PlayerComposite(final IPlayerComposite wrapped) {
		super(wrapped);
	}

	public long getUid() {
		if (!isNull()) {
			return get().getUid();
		}

		return -1;
	}

	public int[] getAppearance() {
		if (!isNull()) {
			return get().getAppearance();
		}

		return null;
	}
}
