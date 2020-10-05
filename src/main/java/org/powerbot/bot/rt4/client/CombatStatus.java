package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.ICombatStatus;

public class CombatStatus extends Proxy<ICombatStatus> {

	public CombatStatus(final ICombatStatus wrapped) {
		super(wrapped);
	}

	public LinkedList getList() {
		if (!isNull()) {
			return new LinkedList(wrapped.get().getList());
		}

		return null;
	}

	public BarComponent getBarComponent() {
		if (!isNull()) {
			return new BarComponent(wrapped.get().getBarComponent());
		}

		return null;
	}
}
