package org.powerbot.script;

import java.awt.*;
import java.util.EventListener;

/**
 * PaintListener
 * A listener that listens for canvas repainting events.
 */
public interface PaintListener extends EventListener {
	/**
	 * Response fired upon the canvas requesting to be repainting.
	 *
	 * @param graphics the graphics context used to paint to
	 */
	void repaint(Graphics graphics);
}
