package org.powerbot.script.action;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.awt.*;

public class ObjectAction extends AbstractAction<ObjectAction> {
	private int id;
	private Tile tile;
	private int interactionIndex;

	public int getInteractionIndex() {
		return interactionIndex;
	}

	public ObjectAction setInteractionIndex(int interactionIndex) {
		this.interactionIndex = interactionIndex;
		return this;
	}

	public int getId() {
		return id;
	}

	public ObjectAction setId(int id) {
		this.id = id;
		return this;
	}

	public Tile getTile() {
		return tile;
	}

	public ObjectAction setTile(Tile tile) {
		this.tile = tile;
		return this;
	}

}
