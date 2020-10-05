package org.powerbot.bot;

import org.powerbot.script.*;

import java.awt.*;
import java.awt.geom.Line2D;

public class ViewMouse<C extends ClientContext> extends ClientAccessor<C> implements PaintListener {

	public ViewMouse(final C ctx) {
		super(ctx);
	}

	@Override
	public void repaint(final Graphics render) {
		final Graphics2D g2 = (Graphics2D) render;
		final Point p = ctx.input.getLocation();
		final int l = 6;

		g2.setColor(Color.CYAN);
		g2.setStroke(new BasicStroke(1.5f));
		g2.draw(new Line2D.Float(p.x - l, p.y - l, p.x + l, p.y + l));
		g2.draw(new Line2D.Float(p.x + l, p.y - l, p.x - l, p.y + l));

		if (System.currentTimeMillis() - ctx.input.getPressWhen() < 1000) {
			final Point px = ctx.input.getPressLocation();
			g2.setColor(Color.RED);
			g2.drawLine(px.x - l, px.y - l, px.x + l, px.y + l);
			g2.drawLine(px.x + l, px.y - l, px.x - l, px.y + l);
		}
	}
}
