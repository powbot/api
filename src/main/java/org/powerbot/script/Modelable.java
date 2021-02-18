package org.powerbot.script;

import org.powerbot.bot.rt4.HashTable;
import org.powerbot.bot.rt4.client.Cache;
import org.powerbot.bot.rt4.client.internal.IModel;
import org.powerbot.bot.rt4.client.internal.INode;
import org.powerbot.bot.rt4.client.internal.INpc;
import org.powerbot.bot.rt4.client.internal.IRenderable;
import org.powerbot.script.rt4.CacheModelConfig;
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

	default long getModelCacheId() {
		return -1L;
	}

	default Cache getModelCache() {
		return null;
	}
	/**
	 * Load the model from the cache
	 * @return model
	 */
	default Model model() {
		try {
			if (renderable() instanceof IModel) {
				final IModel renderableModel = (IModel) renderable();
				return new Model(ctx(), renderableModel.getVerticesX().clone(), renderableModel.getVerticesY().clone(),
					renderableModel.getVerticesZ().clone(), renderableModel.getIndicesX().clone(), renderableModel.getIndicesY().clone(),
					renderableModel.getIndicesZ().clone(), modelOrientation());
			}

			if (!ctx().bot().disableModelAnimations()) {
				Model model = ctx().modelCache.getModel(ctx(), renderable(), isAnimated());
				if (model == null && renderable() instanceof IModel) {
					final IModel renderableModel = (IModel) renderable();
					ctx().modelCache.onRender(renderable(), renderableModel.getVerticesX().clone(), renderableModel.getVerticesY().clone(),
						renderableModel.getVerticesZ().clone(), renderableModel.getIndicesX().clone(), renderableModel.getIndicesY().clone(),
						renderableModel.getIndicesZ().clone(), modelOrientation());

					return ctx().modelCache.getModel(ctx(), renderable(), isAnimated());
				}
				return null;
			}

			final Cache cache = getModelCache();
			final long modelCacheId = getModelCacheId();
			if (cache != null && modelCacheId > 0) {
				final org.powerbot.bot.rt4.HashTable<INode> table = new HashTable<>(cache.get().getTable());
				final INode modelNode = table.lookup(modelCacheId);
				if (modelNode instanceof IModel) {
					return new Model(ctx(), ((IModel) modelNode).getVerticesX().clone(),
						((IModel) modelNode).getVerticesY().clone(), ((IModel) modelNode).getVerticesZ().clone(),
						((IModel) modelNode).getIndicesX().clone(), ((IModel) modelNode).getIndicesY().clone(),
						((IModel) modelNode).getIndicesZ().clone(), modelOrientation());
				}
			}

			int[] modelIds = modelIds();
			if (modelIds != null) {
				CacheModelConfig cacheModel = ctx().bot().getCacheWorker().modelConfigLoader().byIds(modelIds);
				if (cacheModel != null && cacheModel.valid()) {
					return new Model(ctx(), cacheModel.verticesX, cacheModel.verticesY, cacheModel.verticesZ,
						cacheModel.indicesX, cacheModel.indicesY, cacheModel.indicesZ, modelOrientation()
					);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
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
