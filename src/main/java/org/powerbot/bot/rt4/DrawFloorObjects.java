package org.powerbot.bot.rt4;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public class DrawFloorObjects extends DrawObjects {
	public DrawFloorObjects(final ClientContext ctx) {
		super(ctx, GameObject.Type.FLOOR_DECORATION);
	}
}
