package org.powerbot.bot;

import java.awt.*;

public class DebugHelper {
	public static void drawLine(final Graphics render, final int row, final String text) {
		final int height = render.getFontMetrics().getHeight() + 4;
		final int x = 7, y = row * height + height + 19;
		render.setColor(Color.GREEN);
		render.drawString(text, x, y);
	}
}
