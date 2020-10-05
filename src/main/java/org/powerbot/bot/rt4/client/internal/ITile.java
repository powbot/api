package org.powerbot.bot.rt4.client.internal;

public interface ITile extends INode {

	IBoundaryObject getBoundaryObject();

	IFloorObject getFloorObject();

	int getGameObjectLength();

	IGameObject[] getGameObjects();

	IItemPile getItemPile();

	IWallObject getWallObject();

}