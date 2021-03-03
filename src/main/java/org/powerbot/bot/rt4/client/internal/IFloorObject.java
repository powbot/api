package org.powerbot.bot.rt4.client.internal;

public interface IFloorObject extends IBasicObject {

	IRenderable getRenderable();

	@Override
	default IRenderable[] getRenderables() {
		return new IRenderable[]{getRenderable()};
	}
}
