package org.powerbot.bot.rt4;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public class DrawOtherObjects extends DrawObjects {
	public DrawOtherObjects(final ClientContext ctx) {
		super(ctx, GameObject.Type.UNKNOWN);
	}
}
