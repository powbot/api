package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IGameObject;
import org.powerbot.bot.rt4.client.internal.INodeDeque;
import org.powerbot.bot.rt4.client.internal.ITile;
import org.powerbot.script.rt4.GroundItem;

public class Tile extends Proxy<ITile> {

	public Tile(final ITile wrapped) {
		super(wrapped);
	}

	public ItemPile getItemPile() {
		if (!isNull()) {
			return new ItemPile(wrapped.get().getItemPile());
		}

		return null;
	}

	public BoundaryObject getBoundaryObject() {
		if (!isNull()) {
			return new BoundaryObject(wrapped.get().getBoundaryObject());
		}

		return null;
	}

	public WallObject getWallObject() {
		if (!isNull()) {
			return new WallObject(wrapped.get().getWallObject());
		}

		return null;
	}

	public FloorObject getFloorObject() {
		if (!isNull()) {
			return new FloorObject(wrapped.get().getFloorObject());
		}

		return null;
	}

	public GameObject[] getGameObjects() {
		if (!isNull()) {
			final IGameObject[] objects = wrapped.get().getGameObjects();
			final GameObject[] wrapped = objects != null ? new GameObject[objects.length] : null;
			if (objects != null) {
				for (int i = 0; i < objects.length; i++) {
					wrapped[i] = new GameObject(objects[i]);
				}
			}
			return wrapped;
		}

		return null;
	}

	public int getGameObjectLength() {
		if (!isNull()) {
			return wrapped.get().getGameObjectLength();
		}

		return -1;
	}
}
