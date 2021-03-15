package org.powerbot.bot.rt4.client.internal;

public interface IBoundaryObject extends IBasicObject {

	int getOrientation1();

	int getOrientation2();

	IRenderable getRenderable1();

	IRenderable getRenderable2();

	@Override
	default int getOrientation() {
		return getOrientation1();
	}

	@Override
	default int[] modelOrientations() {
		return new int[]{getOrientation1(), getOrientation2()};
	}

	@Override
	default IRenderable[] getRenderables() {
		return new IRenderable[]{getRenderable1(), getRenderable2()};
	}
}
