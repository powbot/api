package org.powerbot.script;

import org.powerbot.bot.rt4.client.internal.IModel;
import org.powerbot.bot.rt4.client.internal.INpc;
import org.powerbot.bot.rt4.client.internal.IRenderable;
import org.powerbot.script.rt4.Model;

import java.awt.*;
import java.util.Arrays;

/**
 * Modelable
 * An entity which contains a model.
 */
public interface Modelable {

	/**
	 * The local X position of the entity
	 * @return local x
	 */
	int localX();

	/**
	 * The local Y position of the entity
	 * @return local y
	 */
	int localY();

	/**
	 * The orientation of the entity (which way it's facing)
	 * @return model orientation
	 */
	int modelOrientation();

	/**
	 * Model ids to load from the cache
	 * @return model ids
	 * @deprecated - not required anymore
	 */
	int[] modelIds();

	org.powerbot.script.rt4.ClientContext ctx();

	IRenderable renderable();

	/**
	 * Whether or not the model is animated
	 * @return true if the model is animated
	 */
	boolean isAnimated();

	/**
	 * Load the model from the cache
	 * @return model
	 */
	default Model model() {
		Model model = ctx().modelCache.getModel(ctx(), renderable(), isAnimated());
		if (model == null && renderable() instanceof IModel) {
			final IModel renderableModel = (IModel) renderable();
			ctx().modelCache.onRender(renderable(), renderableModel.getVerticesX().clone(), renderableModel.getVerticesY().clone(),
				renderableModel.getVerticesZ().clone(), renderableModel.getIndicesX().clone(), renderableModel.getIndicesY().clone(),
				renderableModel.getIndicesZ().clone(), modelOrientation());

			model = ctx().modelCache.getModel(ctx(), renderable(), isAnimated());
		}
		return model;
	}

	default Polygon hull() {
		final Model model = model();
		if (model != null) {
			return model.quickHull(localX(), localY());
		}

		return null;
	}

	/**
	 * Draws the model polygons on the screen for debug purposes
	 * @param g - Graphics object to onRender with
	 */
	default void drawModel(final Graphics g) {
		final Model model = model();
		if (model != null) {
			model.draw(localX(), localY(), g);
		}
	}

	/**
	 * Get the center point of the model
	 * @return center point
	 */
	default Point modelCenterPoint() {
		final Model model = model();
		if (model == null) {
			return new Point(-1, -1);
		}
		return model.centerPoint(localX(), localY());
	}
}
