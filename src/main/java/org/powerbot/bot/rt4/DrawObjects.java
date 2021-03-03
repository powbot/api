package org.powerbot.bot.rt4;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DrawObjects extends ClientAccessor implements PaintListener {
	private static final Color[] C = {Color.GREEN, Color.WHITE, Color.BLACK, Color.BLUE, Color.PINK};
	private final GameObject.Type type;

	protected DrawObjects(final ClientContext ctx, final GameObject.Type type) {
		super(ctx);
		this.type = type;
	}

	@Override
	public void repaint(final Graphics render) {
		try {
			if (ctx.game.clientState() != Constants.GAME_LOADED) {
				return;
			}
			final Player player = ctx.players.local();
			if (player == null) {
				return;
			}
			final FontMetrics metrics = render.getFontMetrics();
			final int textHeight = metrics.getHeight();

			for (final GameObject object : ctx.objects.toStream()
				.within(8).filter(o -> o.type() == type)
				.collect(Collectors.toList())) {
				final Tile t = object.tile();
				if (t == null) {
					continue;
				}
				if (!object.inViewport()) {
					continue;
				}

				final Point p = object.tile().distanceTo(player) > 4 ? object.basePoint() : object.centerPoint();
				if (p.x == -1) {
					continue;
				}

				if (object.tile().distanceTo(player) <= 2) {
					object.drawModel(render);
				}
				final Polygon hull = object.hull();
				if (hull != null && object.tile().distanceTo(player) <= 4) {
					render.drawPolygon(hull);
				}

				render.setColor(Color.black);
				render.fillRect(p.x - 1, p.y - 1, 2, 2);

				final String s = Integer.toString(object.id());
				final int ty = p.y - textHeight / 2;
				final int tx = p.x - metrics.stringWidth(s) / 2;
				render.setColor(C[object.type().ordinal()]);
				final StringBuilder b = new StringBuilder(s);
				final String n = object.name();
				if (!n.isEmpty() && !n.equals("null")) {
					int[] arr = object.meshIds();
					if (arr.length < 1) {
						arr = new int[]{-1};
					}
					b.append(" (").append(n).append('/').append(arr[0]).append(')');
				}
				b.append(" - ").append(object.orientation())
					.append("/").append(object.meta());
				if (object.tile().distanceTo(player) < 3) {
					b.append("/").append(object.tile().toString());
				}
				render.drawString(b.toString(), tx, ty - textHeight);
			}
		} catch (final Throwable t) {
			t.printStackTrace();
		}
	}
}
