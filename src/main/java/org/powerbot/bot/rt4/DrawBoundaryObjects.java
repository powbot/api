package org.powerbot.bot.rt4;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public class DrawBoundaryObjects extends DrawObjects {
	public DrawBoundaryObjects(final ClientContext ctx) {
		super(ctx, GameObject.Type.BOUNDARY);
	}
}
