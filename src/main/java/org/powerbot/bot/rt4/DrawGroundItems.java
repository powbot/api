package org.powerbot.bot.rt4;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DrawGroundItems extends ClientAccessor implements PaintListener {

	public DrawGroundItems(final ClientContext ctx) {
		super(ctx);
	}

	public void repaint(final Graphics render) {
		if (ctx.game.clientState() != Constants.GAME_LOADED) {
			return;
		}

		final Player player = ctx.players.local();
		if (player == null) {
			return;
		}
		final Tile tile = player.tile();
		if (tile == null) {
			return;
		}

		final FontMetrics metrics = render.getFontMetrics();
		final int tHeight = metrics.getHeight();
		final List<GroundItem> check = new ArrayList<>();
		ctx.groundItems.select(10).addTo(check);
		for (int x = -10; x <= 10; x++) {
			for (int y = -10; y <= 10; y++) {
				int d = 0;
				final Tile loc = tile.derive(x, y);
				final Point screen = new TileMatrix(ctx, loc).centerPoint();
				if (screen.x == -1) {
					continue;
				}
				for (final GroundItem groundItem : ctx.groundItems.select(check).at(loc)) {
					final String name = groundItem.name();
					String s = "";
					s += groundItem.id();
					if (!name.isEmpty()) {
						s += " " + name;
					}
					final int stack = groundItem.stackSize();
					if (stack > 1) {
						s += " (" + stack + ")";
					}
					final int ty = screen.y - tHeight * (++d) + tHeight / 2;
					final int tx = screen.x - metrics.stringWidth(s) / 2;
					render.setColor(Color.green);
					render.drawString(s, tx, ty);
				}
			}
		}
	}
}
