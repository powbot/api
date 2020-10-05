package org.powerbot.script;

import java.awt.*;

/**
 * Drawable
 * An entity which is rendered at varying opacity in the viewport.
 */
public interface Drawable {
	/**
	 * Renders the entity within the viewport at {@code alpha = 255}
	 *
	 * @param render the graphics context to use for rendering
	 */
	void draw(Graphics render);

	/**
	 * Renders the entity within the viewport at the specified alpha.
	 *
	 * @param render the graphics context to use for rendering
	 * @param alpha  the alpha level, {@code 0 <= alpha <= 255}
	 */
	void draw(Graphics render, int alpha);
}
