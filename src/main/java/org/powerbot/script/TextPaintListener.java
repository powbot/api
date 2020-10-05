package org.powerbot.script;

import java.awt.*;
import java.util.EventListener;

/**
 * TextPaintListener
 * A listener for a text event of a certain index within an array of string being painted on the game canvas.
 */
public interface TextPaintListener extends EventListener {
	/**
	 * Response fired upon an index being updated and needing to be repainted.
	 *
	 * @param idx      the index of the string to be repainted
	 * @param graphics the graphics context to paint to
	 * @return the next index to repaint
	 */
	int draw(int idx, Graphics graphics);
}
