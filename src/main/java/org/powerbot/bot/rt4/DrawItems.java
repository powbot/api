package org.powerbot.bot.rt4;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.GeItem;

import java.awt.*;

import static org.powerbot.script.rt4.Item.HEIGHT;
import static org.powerbot.script.rt4.Item.WIDTH;

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

			final Rectangle bound = item.boundingRect();
			render.drawRect(bound.x, bound.y, bound.width, bound.height);

			render.setColor(Color.red);
			final Rectangle clickingBound = new Rectangle(bound.x + bound.width / 3, bound.y + bound.height / 3, bound.width / 3, bound.height / 3);
			render.drawRect(clickingBound.x, clickingBound.y, clickingBound.width, clickingBound.height);

			render.setColor(Color.green);
			final Point p = item.centerPoint();
			p.translate(-21, -18);
			render.drawString(item.id() + "", p.x, p.y + 36);
		}
	}
}
