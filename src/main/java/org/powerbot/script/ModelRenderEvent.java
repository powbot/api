package org.powerbot.script;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IRenderable;

import java.util.EventListener;

/**
 * ModelDrawEvent
 * An event that is dispatched when the game renders a model.
 */
public class ModelRenderEvent extends AbstractEvent {
	public static final int MODEL_DRAW_EVENT = 0x120;
	private static final long serialVersionUID = 2905739551668844907L;
	private final IRenderable renderable;
	private int[] verticesX;
	private int[] verticesY;
	private int[] verticesZ;
	private int[] indicesX;
	private int[] indicesY;
	private int[] indicesZ;

	public ModelRenderEvent(final IRenderable renderable, final int[] verticesX, final int[] verticesY, final int[] verticesZ, final int[] indicesX, final int[] indicesY, final int[] indicesZ) {
		super(MODEL_DRAW_EVENT);
		this.renderable = renderable;
		this.verticesX = verticesX;
		this.verticesY = verticesY;
		this.verticesZ = verticesZ;
		this.indicesX = indicesX;
		this.indicesY = indicesY;
		this.indicesZ = indicesZ;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void call(final EventListener e) {
		try {
			((ModelRenderListener) e).onRender(renderable, verticesX, verticesY, verticesZ, indicesX, indicesY, indicesZ);
		} catch (final Exception ignored) {
		}
	}
}
