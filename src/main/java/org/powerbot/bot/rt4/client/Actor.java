package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IActor;

public class Actor<T extends IActor> extends Proxy<T> {

	public Actor(final T wrapped) {
		super(wrapped);
	}

	public int getX() {
		if (!isNull()) {
			return wrapped.get().getX();
		}

		return -1;
	}

	public int getZ() {
		if (!isNull()) {
			return wrapped.get().getZ();
		}

		return -1;
	}

	public int getHeight() {
		if (!isNull()) {
			return wrapped.get().getHeight();
		}

		return -1;
	}

	public int getAnimation() {
		if (!isNull()) {
			return wrapped.get().getAnimation();
		}

		return -1;
	}

	public int getSpeed() {
		if (!isNull()) {
			return wrapped.get().getSpeed();
		}

		return -1;
	}

	public String getOverheadMessage() {
		if (!isNull()) {
			return wrapped.get().getOverheadMessage();
		}

		return null;
	}

	public int getOrientation() {
		if (!isNull()) {
			return wrapped.get().getOrientation();
		}

		return -1;
	}

	public int getInteractingIndex() {
		if (!isNull()) {
			return wrapped.get().getInteractingIndex();
		}

		return -1;
	}

	public LinkedList getCombatStatusList() {
		if (!isNull()) {
			return new LinkedList(wrapped.get().getCombatStatusList());
		}

		return null;
	}
}
