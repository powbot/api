package org.powerbot.bot.rt4.client;

import org.powerbot.bot.Proxy;
import org.powerbot.bot.rt4.client.internal.IRenderable;

public abstract class BasicObject<T> extends Proxy<T> {

	public BasicObject(final T wrapped) {
		super(wrapped);
	}

	public abstract long getUid();

	public abstract int getMeta();

	public abstract int getX();

	public abstract int getZ();

	public abstract int getX1();

	public abstract int getY1();

	public abstract int getX2();

	public abstract int getY2();

	@Deprecated
	public abstract IRenderable getRenderable();

	public IRenderable[] getRenderables() {
		return null;
	}

	public abstract int[] modelOrientations();

	public abstract int getOrientation();
}
