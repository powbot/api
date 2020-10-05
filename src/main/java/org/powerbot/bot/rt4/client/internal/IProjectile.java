package org.powerbot.bot.rt4.client.internal;

public interface IProjectile extends IRenderable {

	int getCycleEnd();

	int getCycleStart();

	int getEndHeight();

	int getId();

	int getOrientation();

	int getPlane();

	int getSlope();

	int getStartDistance();

	int getStartX();

	int getStartY();

	int getTargetIndex();

	double getX();

	double getY();

	boolean isStarted();

}