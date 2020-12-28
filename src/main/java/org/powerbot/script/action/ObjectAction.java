package org.powerbot.script.action;

import org.powerbot.bot.rt4.client.Tile;

public class ObjectAction extends AbstractAction {
	private int id;
	private Tile tile;

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
