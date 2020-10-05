package org.powerbot.bot.rt4;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DrawProjectiles extends ClientAccessor implements PaintListener {
	private static final Color[] C = {Color.GREEN, Color.WHITE, Color.BLACK, Color.BLUE, Color.PINK};

	public DrawProjectiles(final ClientContext ctx) {
		super(ctx);
	}

	@Override
	public void repaint(final Graphics render) {
		if (ctx.game.clientState() != Constants.GAME_LOADED) {
			return;
		}
		final Player player = ctx.players.local();
		if (player == null) {
			return;
		}
		final FontMetrics metrics = render.getFontMetrics();
		final int textHeight = metrics.getHeight();

		final Map<Tile, AtomicInteger> counts = new HashMap<>();
		for (final Projectile o : ctx.projectiles.select()) {
			final Tile t = new Tile(o.getX(), o.getY(), ctx.client().getFloor());
			if (!counts.containsKey(t)) {
				counts.put(t, new AtomicInteger(0));
			}
			final Point p = t.matrix(ctx).centerPoint();
			if (p.x == -1) {
				continue;
			}

			render.setColor(Color.black);
			render.fillRect(p.x - 1, p.y - 1, 2, 2);

			final String s = String.format("[%s] INT: %s", o.id(), o.getTarget());
			final int ty = p.y - textHeight / 2;
			final int tx = p.x - metrics.stringWidth(s) / 2;

			if (o.getTarget().equals(ctx.players.local())) {
				render.setColor(C[4]);
			} else {
				render.setColor(C[0]);
			}
			render.drawString(s, tx, ty - textHeight * counts.get(t).getAndIncrement());

		}
	}
}
