package org.powerbot.script;

import org.powerbot.bot.rt4.client.internal.IModel;
import org.powerbot.bot.rt4.client.internal.IRenderable;

import java.util.EventListener;

/**
 * ModelDrawListener
 * A listener for models being rendered in game
 */
public interface ModelRenderListener extends EventListener {
	/**
	 * Fired upon a model being rendered in game
	 *
	 * @param renderable - the renderable entity being rendered
	 * @return the next index to repaint
	 */
	void onRender(final IRenderable renderable, final int[] verticesX, final int[] verticesY, final int[] verticesZ, final int[] indicesX, final int[] indicesY, final int[] indicesZ, final int orientation);
}
