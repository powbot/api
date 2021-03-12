package org.powerbot.bot.rt4.client.internal;

import org.powerbot.bot.rt4.HashTable;
import org.powerbot.bot.rt4.client.extended.IMobileClient;
import org.powerbot.script.*;
import org.powerbot.script.ClientContext;
import org.powerbot.script.Interactive;
import org.powerbot.script.action.Emittable;
import org.powerbot.script.action.ObjectAction;
import org.powerbot.script.rt4.*;
import org.powerbot.util.ScreenPosition;

import java.awt.*;
import java.util.concurrent.Callable;

import static org.powerbot.script.rt4.Interactive.NIL_POINT;

public interface IBasicObject extends Interactive, Actionable, Identifiable, Nameable, Modelable, InteractiveEntity,
	Emittable<ObjectAction>, Nillable<IBasicObject> {

	long getUid();

	int getMeta();

	int getX();

	int getY();

	default int getX1() {
		return -1;
	}

	default int getY1() {
		return -1;
	}

	default int getX2() {
		return -1;
	}

	default int getY2() {
		return -1;
	}

	int getZ();

	IRenderable[] getRenderables();

	default int getOrientation() {
		return 0;
	}

	default int[] modelOrientations() {
		return null;
	}

	@Override
	default void draw(Graphics render) {
		drawModel(render);
	}

	@Override
	default void draw(Graphics render, int alpha) {
		draw(render);
	}

	@Override
	default Point basePoint() {
		return ctx().game.worldToScreen(localX(), localY(), 0);
	}

	@Override
	default Point centerPoint() {
		final Point center = modelCenterPoint();
		if (center != null && center.x > -1) {
			return center;
		}
		return basePoint();
	}

	@Override
	default Callable<Point> calculateScreenPosition() {
		return ScreenPosition.of(ctx(), this);
	}

	@Override
	default Point nextPoint() {
		final Model model = model();
		if (model != null) {
			final Point next = model.nextPoint(localX(), localY());
			if (!next.equals(NIL_POINT)) {
				return next;
			}
		}

		return basePoint();
	}

	@Override
	default boolean contains(final Point point) {
		final Model model = model();
		if (model == null || model.nextPoint(localX(), localY()).equals(NIL_POINT)) {
			return false;
		}
		return model.contains(point, localX(), localY());
	}

	@Override
	default boolean valid() {
		return true;
	}

	@Override
	default boolean inViewport() {
		return ctx().game.inViewport(basePoint()) || ctx().game.inViewport(centerPoint());
	}

	@Override
	String[] actions();

	default int rawId() {
		final long l = getUid();
		final int x = (int) l & 0x7f, z = (int) ((l >> 7) & 0x7f), i = (int) (l >> 17);
		return i << 14 | z << 7 | x;
	}

	default boolean isComplex() {
		return getX() != -1;
	}

	default int mainId() {
		return (rawId() >> 14) & 0xffff;
	}

	@Override
	default int id() {
		final org.powerbot.script.rt4.ClientContext ctx = ctx();
		final IClient client = ctx.client();
		if (client == null) {
			return -1;
		}
		final int id = (rawId() >> 14) & 0xffff;
		int index = -1;
		final CacheObjectConfig c = CacheObjectConfig.load(ctx.bot().getCacheWorker(), id);
		if (c == null) {
			return id;
		}

		if (c.stageOperationId != -1) {
			if (ctx.client().isMobile()) {
				index = ((IMobileClient) ctx.client()).getObjectConfigIndex(c.stageOperationId);
			} else {
				final ICache cache = client.getVarbitCache();
				final HashTable<INode> table = new HashTable<>(cache.getTable());
				final INode varbitNode = table.lookup(c.stageOperationId);
				if (varbitNode instanceof IVarbit) {
					final IVarbit varBit = (IVarbit) varbitNode;
					final int mask = GameObject.lookup[varBit.getEndBit() - varBit.getStartBit()];
					index = ctx.varpbits.varpbit(varBit.getIndex()) >> varBit.getStartBit() & mask;
				} else {
					final CacheVarbitConfig cachedVarbit = CacheVarbitConfig.load(ctx.bot().getCacheWorker(), c.stageOperationId);
					final int mask = GameObject.lookup[cachedVarbit.endBit - cachedVarbit.startBit];
					index = ctx.varpbits.varpbit(cachedVarbit.configId) >> cachedVarbit.startBit & mask;
				}
			}
		} else if (c.stageIndex >= 0) {
			index = ctx.varpbits.varpbit(c.stageIndex);
		}

		if (index >= 0) {
			final int[] configs = c.materialPointers;
			if (configs != null && index < configs.length && configs[index] != -1) {
				return configs[index];
			}
		}
		return id;
	}

	@Override
	default String name() {
		final String raw = rawName();
		return raw != null ? StringUtils.stripHtml(raw) : "";
	}

	default String rawName() {
		final int id = (rawId() >> 14) & 0xffff;
		final CacheObjectConfig
			c1 = CacheObjectConfig.load(ctx().bot().getCacheWorker(), id),
			c2 = CacheObjectConfig.load(ctx().bot().getCacheWorker(), id());
		if (c2 != null) {
			if (c1 != null && c2.name.equals("null")) {
				return c1.name;
			}
			return c2.name;
		} else if (c1 != null) {
			return c1.name;
		}
		return "";
	}

	@Override
	default Tile tile() {
		final IClient client = ctx().client();
		final int r = relative();
		final int rx = r >> 16, rz = r & 0xffff;
		if (client != null && rx != 0 && rz != 0) {
			return new Tile(client.getOffsetX() + (rx >> 7), client.getOffsetY() + (rz >> 7), client.getFloor());
		}
		return Tile.NIL;
	}

	default int relative() {
		final int x, z;
		if (isComplex()) {
			x = getX();
			z = getZ();
		} else {
			final int uid = rawId();
			x = (uid & 0x7f) << 7;
			z = ((uid >> 7) & 0x7f) << 7;
		}
		return (x << 16) | z;
	}

	@Override
	default int localX() {
		if (getX() != -1) {
			return getX();
		}

		final int r = relative();
		return r >> 16;
	}

	@Override
	default int localY() {
		if (getZ() != -1) {
			return getZ();
		}

		final int r = relative();
		return r & 0xffff;
	}

	@Override
	default int[] modelIds() {
		final CacheObjectConfig c = CacheObjectConfig.load(ctx().bot().getCacheWorker(), id());
		return c != null ? c.meshId : null;
	}

	@Override
	default org.powerbot.script.rt4.ClientContext ctx() {
		return ClientContext.ctx();
	}

	@Override
	default IRenderable[] renderables() {
		return getRenderables();
	}

	@Override
	default long getModelCacheId() {
		int[] orientations = modelOrientations();

		int type = getMeta() & 0x3f;
		int face =  orientations != null ? orientations[0] : 0;
		int id = id();
		final CacheObjectConfig c = CacheObjectConfig.load(ctx().bot().getCacheWorker(), id);
		if (c == null || c.meshType == null) {
			return ((long) id << 10) + face;
		}

		return face + (type << 3) + ((long) id << 10);
	}

	@Override
	default ICache getModelCache() {
		final IClient client = ctx().client();
		if (client == null) {
			return null;
		}

		return client.getObjectModelCache();
	}


	default short[] originalColors() {
		final CacheObjectConfig c = CacheObjectConfig.load(ctx().bot().getCacheWorker(), id());
		if (c != null && c.originalColors != null) {
			return c.originalColors;
		}
		return new short[0];
	}

	default short[] modifiedColors() {
		final CacheObjectConfig c = CacheObjectConfig.load(ctx().bot().getCacheWorker(), id());
		if (c != null && c.modifiedColors != null) {
			return c.modifiedColors;
		}
		return new short[0];
	}

	default int width() {
		final CacheObjectConfig c = CacheObjectConfig.load(ctx().bot().getCacheWorker(), id());
		if (c != null) {
			return c.xSize;
		}
		return -1;
	}

	default int height() {
		final CacheObjectConfig c = CacheObjectConfig.load(ctx().bot().getCacheWorker(), id());
		if (c != null) {
			return c.ySize;
		}
		return -1;
	}

	default int[] meshIds() {
		final CacheObjectConfig c = CacheObjectConfig.load(ctx().bot().getCacheWorker(), id());
		if (c != null) {
			int[] meshIds = c.meshId;
			if (meshIds == null) {
				meshIds = new int[0];
			}
			return meshIds;
		}
		return new int[0];
	}

	default int orientation() {
		return getMeta() >> 6;
	}

	@Override
	default ObjectAction createAction(String action, boolean async) {
		final Point p = nextPoint();
		if (p == null || p.x == -1) {
			return null;
		}
		final String[] actions = actions();
		if (actions == null) {
			return null;
		}
		int interactionIndex = -1;
		for (int i = 0; i < actions.length; i++) {
			if (actions[i].equalsIgnoreCase(action)) {
				interactionIndex = i;
				break;
			}
		}
		if (interactionIndex == -1) {
			return null;
		}

		return new ObjectAction()
			.setTile(tile())
			.setId(mainId())
			.setMouseX(p.x)
			.setMouseY(p.y)
			.setEntityName(name())
			.setInteractionIndex(interactionIndex)
			.setInteraction(action)
			.setAsync(async);
	}

	@Override
	default IBasicObject nil() {
		return Objects.NIL;
	}
}
