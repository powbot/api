package org.powerbot.bot.rt4.client.internal;

public interface IBasicObject {

	long getUid();

	int getMeta();

	int getX();

	int getY();

	default int getX1() {
		return -1;
	}

	default int getY1() {
		return -1;
	}

	default int getX2() {
		return -1;
	}

	default int getY2() {
		return -1;
	}

	int getZ();

	IRenderable[] getRenderables();

	default int getOrientation() {
		return 0;
	}

	default int[] modelOrientations() {
		return null;
	}

}
