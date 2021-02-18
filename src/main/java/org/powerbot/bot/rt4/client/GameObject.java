package org.powerbot.bot.rt4.client;

import org.powerbot.bot.rt4.client.internal.IGameObject;
import org.powerbot.bot.rt4.client.internal.IRenderable;

public class GameObject extends BasicObject<IGameObject> {

	public GameObject(final IGameObject wrapped) {
		super(wrapped);
	}

	@Override
	public long getUid() {
		if (!isNull()) {
			return get().getUid();
		}

		return -1L;
	}

	@Override
	public int getMeta() {
		if (!isNull()) {
			return get().getMeta();
		}

		return -1;
	}

	@Override
	public int getX() {
		if (!isNull()) {
			return get().getX();
		}

		return -1;
	}

	@Override
	public int getZ() {
		if (!isNull()) {
			return get().getZ();
		}

		return -1;
	}

	@Override
	public int getX1() {
		if (!isNull()) {
			return get().getX1();
		}

		return -1;
	}

	@Override
	public int getY1() {
		if (!isNull()) {
			return get().getY1();
		}

		return -1;
	}

	@Override
	public int getX2() {
		if (!isNull()) {
			return get().getX2();
		}

		return -1;
	}

	@Override
	public int getY2() {
		if (!isNull()) {
			return get().getY2();
		}

		return -1;
	}

	@Override
	public IRenderable getRenderable() {
		if (!isNull()) {
			return get().getRenderable();
		}
		return null;
	}

	@Override
	public int getOrientation() {
		if (!isNull()) {
			return get().getOrientation();
		}

		return -1;
	}

	@Override
	public int[] modelOrientations() {
		if (!isNull()) {
			return new int[]{get().getOrientation()};
		}

		return new int[]{-1};
	}

	@Override
	public IRenderable[] getRenderables() {
		if (!isNull()) {
			return new IRenderable[]{get().getRenderable()};
		}
		return null;
	}
}
