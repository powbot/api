package org.powerbot.bot;

import org.powerbot.script.*;

import java.awt.*;

import static org.powerbot.bot.DebugHelper.*;

public class TMousePosition<C extends ClientContext> extends ClientAccessor<C> implements TextPaintListener {

	public TMousePosition(final C ctx) {
		super(ctx);
	}

	public int draw(int idx, final Graphics render) {
		final Point p = ctx.input.getLocation();
		drawLine(render, idx++, "Mouse position: " + (int) p.getX() + "," + (int) p.getY());
		return idx;
	}
}
