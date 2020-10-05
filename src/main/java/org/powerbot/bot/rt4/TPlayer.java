package org.powerbot.bot.rt4;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

import java.awt.*;
import java.util.Arrays;

import static org.powerbot.bot.DebugHelper.*;

public class TPlayer extends ClientAccessor implements TextPaintListener {
	public TPlayer(final ClientContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("deprecation")
	public int draw(int idx, final Graphics render) {
		final Player player = ctx.players.local();
		drawLine(render, idx++, String.format("[%s] A: %d, CBL: %d, HP: %d, T: %d, S: %d, INT: %s", player.name(), player.animation(), player.combatLevel(), player.health(), player.team(), player.speed(), player.interacting()));
		drawLine(render, idx++, String.format("ORIENT: %d, COMBAT: %s, APP (VE): %s", player.orientation(), player.inCombat(), Arrays.toString(player.appearance())));

		return idx;
	}
}
