package org.powerbot.bot.rt4;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.awt.*;

import static org.powerbot.bot.DebugHelper.*;

public class TMenu extends ClientAccessor implements TextPaintListener {

	public TMenu(final ClientContext ctx) {
		super(ctx);
	}

	public int draw(int idx, final Graphics render) {
		drawLine(render, idx++, "Menu [x:" + ctx.client().getMenuX() + ", y:" + ctx.client().getMenuY() + ", w:" + ctx.client().getMenuWidth() + ", h:" + ctx.client().getMenuHeight() + ", o:" + ctx.client().isMenuOpen());
		final String[] menuItems = ctx.menu.items();
		for (final String menuItem : menuItems) {
			drawLine(render, idx++, " -> " + menuItem);
		}

		render.drawRect(ctx.client().getMenuX(), ctx.client().getMenuY(), ctx.client().getMenuWidth(), ctx.client().getMenuHeight());
		return idx;
	}
}
