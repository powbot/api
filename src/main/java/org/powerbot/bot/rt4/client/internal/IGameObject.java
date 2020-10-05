package org.powerbot.bot.rt4.client.internal;

public interface IGameObject extends IBasicObject {

	int getMeta();

	int getOrientation();

	IRenderable getRenderable();

	long getUid();

	int getX();

	int getX1();

	int getX2();

	int getY1();

	int getY2();

	int getZ();

}