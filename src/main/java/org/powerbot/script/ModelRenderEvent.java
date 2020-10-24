package org.powerbot.script;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IRenderable;

import java.util.EventListener;

/**
 * ModelDrawEvent
 * An event that is dispatched when the game renders a model.
 */
public class ModelRenderEvent extends AbstractEvent {
	public static final int EVENT_ID = EventType.MODEL_RENDER_EVENT.id();
	private static final long serialVersionUID = 2905739551668844907L;
	private final IRenderable renderable;
	private final int[] verticesX;
	private final int[] verticesY;
	private final int[] verticesZ;
	private final int[] indicesX;
	private final int[] indicesY;
	private final int[] indicesZ;
	private final int orientation;

	public ModelRenderEvent(final IRenderable renderable, final int[] verticesX, final int[] verticesY, final int[] verticesZ, final int[] indicesX, final int[] indicesY, final int[] indicesZ,
							 final int orientation) {
		super(EVENT_ID);
		this.renderable = renderable;
		this.verticesX = verticesX;
		this.verticesY = verticesY;
		this.verticesZ = verticesZ;
		this.indicesX = indicesX;
		this.indicesY = indicesY;
		this.indicesZ = indicesZ;
		this.orientation = orientation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void call(final EventListener e) {
		try {
			((ModelRenderListener) e).onRender(renderable, verticesX, verticesY, verticesZ, indicesX, indicesY, indicesZ, orientation);
		} catch (final Exception ignored) {
		}
	}
}
