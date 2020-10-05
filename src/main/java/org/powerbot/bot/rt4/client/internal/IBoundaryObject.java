package org.powerbot.bot.rt4.client.internal;

public interface IBoundaryObject extends IBasicObject {

	int getMeta();

	int getOrientation1();

	int getOrientation2();

	IRenderable getRenderable1();

	IRenderable getRenderable2();

	long getUid();

}