package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.ICombatStatusData;

public class CombatStatusData extends Proxy<ICombatStatusData> {

	public CombatStatusData(final ICombatStatusData wrapped) {
		super(wrapped);
	}

	public int getHealthRatio() {
		if (!isNull()) {
			return get().getHealthRatio();
		}

		return -1;
	}

	public int getCycleEnd() {
		if (!isNull()) {
			return get().getCycleEnd();
		}

		return -1;
	}
}
