package org.powerbot.script.action;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.awt.*;

public class ObjectAction extends AbstractAction<ObjectAction> {
	private int id;
	private Tile tile;
	private GameObject gameObject;

	public int getId() {
		return id;
	}

	public ObjectAction setId(int id) {
		this.id = id;
		return this;
	}

	public ObjectAction setGameObject(GameObject gameObject) {
		this.gameObject = gameObject;

		if (this.tile == null) {
			this.tile = gameObject.tile();
		}
		if (this.id == 0) {
			this.id = gameObject.mainId();
		}
		if (getMouseX() == 0) {
			Point p = gameObject.nextPoint();
			if (p.x > 0) {
				setMouseX(p.x);
				setMouseY(p.y);
			}
		}
		return this;
	}

	public GameObject getGameObject() {
		return gameObject;
	}

	public Tile getTile() {
		return tile;
	}

	public ObjectAction setTile(Tile tile) {
		this.tile = tile;
		return this;
	}

}
