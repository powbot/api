package org.powerbot.bot.rt4;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;

import static org.powerbot.bot.DebugHelper.*;

public class TOpenTab extends ClientAccessor implements TextPaintListener {

	public TOpenTab(final ClientContext ctx) {
		super(ctx);
	}

	public int draw(int idx, final Graphics render) {
		drawLine(render, idx++, "Open Tab: " + ctx.game.tab().name());
		return idx;
	}
}
