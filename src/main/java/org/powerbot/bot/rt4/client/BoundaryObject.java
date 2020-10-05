package org.powerbot.bot.rt4.client;

import org.powerbot.bot.rt4.client.internal.IBoundaryObject;
import org.powerbot.bot.rt4.client.internal.IRenderable;

public class BoundaryObject extends BasicObject<IBoundaryObject> {

	public BoundaryObject(final IBoundaryObject wrapped) {
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
		return -1;
	}

	@Override
	public int getZ() {
		return -1;
	}

	@Override
	public int getX1() {
		return -1;
	}

	@Override
	public int getY1() {
		return -1;
	}

	@Override
	public int getX2() {
		return -1;
	}

	@Override
	public int getY2() {
		return -1;
	}

	@Override
	public IRenderable getRenderable() {
		if (!isNull()) {
			return wrapped.get().getRenderable1() != null ? wrapped.get().getRenderable1() : wrapped.get().getRenderable2();
		}
		return null;
	}

	@Override
	public int getOrientation() {
		if (!isNull()) {
			return wrapped.get().getOrientation1() > 0 ? wrapped.get().getOrientation1() : wrapped.get().getOrientation2();
		}

		return -1;
	}
}
