package org.powerbot.bot.rt4;

import org.powerbot.script.TextPaintListener;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;

import static org.powerbot.bot.DebugHelper.drawLine;

public class TLoginState extends ClientAccessor implements TextPaintListener {
	public TLoginState(final ClientContext ctx) {
		super(ctx);
	}

	@Override
	public int draw(int idx, final Graphics graphics) {
		drawLine(graphics, idx++, "Login State: " + ctx.game.loginState());
		drawLine(graphics, idx++, "Login Message 1: " + ctx.client().get().getLoginMessage1());
		drawLine(graphics, idx++, "Login Message 2: " + ctx.client().get().getLoginMessage2());
		drawLine(graphics, idx++, "Login Message 3: " + ctx.client().get().getLoginMessage3());
		drawLine(graphics, idx++, "Username: " + ctx.client().get().getUsername());
		return idx;
	}
}
