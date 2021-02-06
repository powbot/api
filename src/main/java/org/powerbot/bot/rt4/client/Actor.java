package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IActor;

public class Actor<T extends IActor> extends Proxy<T> {

	public Actor(final T wrapped) {
		super(wrapped);
	}

	public int getX() {
		if (!isNull()) {
			return get().getX();
		}

		return -1;
	}

	public int getZ() {
		if (!isNull()) {
			return get().getZ();
		}

		return -1;
	}

	public int getHeight() {
		if (!isNull()) {
			return get().getHeight();
		}

		return -1;
	}

	public int getAnimation() {
		if (!isNull()) {
			return get().getAnimation();
		}

		return -1;
	}

	public int getSpeed() {
		if (!isNull()) {
			return get().getSpeed();
		}

		return -1;
	}

	public String getOverheadMessage() {
		if (!isNull()) {
			return get().getOverheadMessage();
		}

		return null;
	}

	public int getOrientation() {
		if (!isNull()) {
			return get().getOrientation();
		}

		return -1;
	}

	public int getInteractingIndex() {
		if (!isNull()) {
			return get().getInteractingIndex();
		}

		return -1;
	}

	public LinkedList getCombatStatusList() {
		if (!isNull()) {
			return new LinkedList(get().getCombatStatusList());
		}

		return null;
	}
}
