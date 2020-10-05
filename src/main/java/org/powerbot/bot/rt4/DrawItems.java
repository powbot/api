package org.powerbot.bot.rt4;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

import java.awt.*;

public class DrawItems extends ClientAccessor implements PaintListener {

	public DrawItems(final ClientContext ctx) {
		super(ctx);
	}

	public void repaint(final Graphics render) {
		if (!ctx.game.loggedIn()) {
			return;
		}

		render.setFont(new Font("Arial", Font.PLAIN, 10));
		render.setColor(Color.green);

		for (final Item item : ctx.inventory.select()) {
			final Point p = item.centerPoint();
			p.translate(-21, -18);
			render.drawString(item.id() + "", p.x, p.y + 36);
		}
	}
}
