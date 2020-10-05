package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IPlayer;

public class Player extends Actor<IPlayer> {

	public Player(final IPlayer wrapped) {
		super(wrapped);
	}

	public int getCombatLevel() {
		if (!isNull()) {
			return wrapped.get().getCombatLevel();
		}

		return -1;
	}

	public String getName() {
		if (!isNull()) {
			return new StringRecord(wrapped.get().getName()).getValue();
		}

		return null;
	}

	public int getTeam() {
		if (!isNull()) {
			return wrapped.get().getTeam();
		}

		return -1;
	}

	public PlayerComposite getComposite() {
		if (!isNull()) {
			return new PlayerComposite(wrapped.get().getComposite());
		}

		return null;
	}
}
