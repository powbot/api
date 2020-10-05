package org.powerbot.bot.rt4;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public class DrawWallObjects extends DrawObjects {
	public DrawWallObjects(final ClientContext ctx) {
		super(ctx, GameObject.Type.WALL_DECORATION);
	}
}
