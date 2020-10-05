package org.powerbot.bot.rt4.client.internal;

public interface IActor extends IRenderable {

	int getAnimation();

	ILinkedList getCombatStatusList();

	int getHeight();

	int getInteractingIndex();

	int getOrientation();

	String getOverheadMessage();

	int getSpeed();

	int getX();

	int getZ();

}