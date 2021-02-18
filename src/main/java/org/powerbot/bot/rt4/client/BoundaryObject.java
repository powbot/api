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
			return get().getRenderable1() != null ? get().getRenderable1() : get().getRenderable2();
		}
		return null;
	}

	@Override
	public int getOrientation() {
		if (!isNull()) {
			return get().getOrientation1() > 0 ? get().getOrientation1() : get().getOrientation2();
		}

		return -1;
	}

	@Override
	public int[] modelOrientations() {
		if (!isNull()) {
			return new int[]{get().getOrientation1(), get().getOrientation2()};
		}

		return new int[]{-1, -1};
	}

	@Override
	public IRenderable[] getRenderables() {
		if (!isNull()) {
			return new IRenderable[]{get().getRenderable1(), get().getRenderable2()};
		}
		return null;
	}
}
