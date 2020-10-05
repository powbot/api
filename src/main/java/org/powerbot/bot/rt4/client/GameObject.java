package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IGameObject;
import org.powerbot.bot.rt4.client.internal.IRenderable;

public class GameObject extends BasicObject<IGameObject> {

	public GameObject(final IGameObject wrapped) {
		super(wrapped);
	}

	@Override
	public long getUid() {
		if (!isNull()) {
			return wrapped.get().getUid();
		}

		return -1L;
	}

	@Override
	public int getMeta() {
		if (!isNull()) {
			return wrapped.get().getMeta();
		}

		return -1;
	}

	@Override
	public int getX() {
		if (!isNull()) {
			return wrapped.get().getX();
		}

		return -1;
	}

	@Override
	public int getZ() {
		if (!isNull()) {
			return wrapped.get().getZ();
		}

		return -1;
	}

	@Override
	public int getX1() {
		if (!isNull()) {
			return wrapped.get().getX1();
		}

		return -1;
	}

	@Override
	public int getY1() {
		if (!isNull()) {
			return wrapped.get().getY1();
		}

		return -1;
	}

	@Override
	public int getX2() {
		if (!isNull()) {
			return wrapped.get().getX2();
		}

		return -1;
	}

	@Override
	public int getY2() {
		if (!isNull()) {
			return wrapped.get().getY2();
		}

		return -1;
	}

	@Override
	public IRenderable getRenderable() {
		if (!isNull()) {
			return wrapped.get().getRenderable();
		}
		return null;
	}

	@Override
	public int getOrientation() {
		if (!isNull()) {
			return wrapped.get().getOrientation();
		}

		return -1;
	}
}
