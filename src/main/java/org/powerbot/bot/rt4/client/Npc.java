package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.INpc;

public class Npc extends Actor<INpc> {

	public Npc(final INpc wrapped) {
		super(wrapped);
	}

	public NpcConfig getConfig() {
		if (!isNull()) {
			return new NpcConfig(wrapped.get().getConfig());
		}

		return null;
	}
}
