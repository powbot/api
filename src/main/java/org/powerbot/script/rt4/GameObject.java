package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.HashTable;
import org.powerbot.bot.rt4.client.Cache;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.bot.rt4.client.Varbit;
import org.powerbot.bot.rt4.client.internal.IRenderable;
import org.powerbot.bot.rt4.client.internal.IVarbit;
import org.powerbot.script.*;
import org.powerbot.util.ScreenPosition;

import java.awt.*;
import java.util.concurrent.Callable;

/**
 * GameObject
 */
public class GameObject extends Interactive implements Nameable, InteractiveEntity, Identifiable, Validatable, Actionable, Modelable {
	public static final Color TARGET_COLOR = new Color(0, 255, 0, 20);
	private static final int[] lookup;

	static {
		lookup = new int[32];
		int i = 2;
		for (int j = 0; j < 32; j++) {
			lookup[j] = i - 1;
			i += i;
		}
	}

	private final BasicObject object;
	private final Type type;
	private final BoundingModel defaultBounds = new BoundingModel(ctx, -32, 32, -64, 0, -32, 32) {
		@Override
		public int x() {
			return relative() >> 16;
		}

		@Override
		public int z() {
			return relative() & 0xffff;
		}
	};

	GameObject(final ClientContext ctx, final BasicObject object, final Type type) {
		super(ctx);
		this.object = object;
		this.type = type;
		boundingModel.set(defaultBounds);
	}

	@Override
	public void bounds(final int x1, final int x2, final int y1, final int y2, final int z1, final int z2) {
		boundingModel.set(new BoundingModel(ctx, x1, x2, y1, y2, z1, z2) {
			@Override
			public int x() {
				return relative() >> 16;
			}

			@Override
			public int z() {
				return relative() & 0xffff;
			}
		});
	}

	public int mainId() {
		final Client client = ctx.client();
		if (client == null) {
			return -1;
		}
		return object != null ? (object.getUid() >> 14) & 0xffff : -1;
	}

	@Override
	public int id() {
		final Client client = ctx.client();
		if (client == null) {
			return -1;
		}
		if (object == null) {
			return -1;
		}
		final int id = (object.getUid() >> 14) & 0xffff;
		int index = -1;
		final CacheObjectConfig c = CacheObjectConfig.load(ctx.bot().getCacheWorker(), id);
		if (c == null) {
			return id;
		}

		if (c.stageOperationId != -1) {
			final Cache cache = client.getVarbitCache();
			final HashTable<IVarbit> table = new HashTable<>(cache.wrapped.get().getTable());
			final Varbit varBit = new Varbit(table.lookup(c.stageOperationId));
			if (!varBit.isNull()) {
				final int mask = lookup[varBit.getEndBit() - varBit.getStartBit()];
				index = ctx.varpbits.varpbit(varBit.getIndex()) >> varBit.getStartBit() & mask;
			} else {
				final CacheVarbitConfig cachedVarbit = CacheVarbitConfig.load(ctx.bot().getCacheWorker(), c.stageOperationId);
				final int mask = lookup[cachedVarbit.endBit - cachedVarbit.startBit];
				index = ctx.varpbits.varpbit(cachedVarbit.configId) >> cachedVarbit.startBit & mask;
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
	public String name() {
		if (object == null) {
			return "";
		}
		final int id = (object.getUid() >> 14) & 0xffff;
		final CacheObjectConfig
			c1 = CacheObjectConfig.load(ctx.bot().getCacheWorker(), id),
			c2 = CacheObjectConfig.load(ctx.bot().getCacheWorker(), id());
		if (c2 != null) {
			if (c1 != null && c2.name.equals("null")) {
				return StringUtils.stripHtml(c1.name);
			}
			return StringUtils.stripHtml(c2.name);
		} else if (c1 != null) {
			return StringUtils.stripHtml(c1.name);
		}
		return "";
	}

	/**
	 * @deprecated Use originalColors() instead
	 */
	@Deprecated
	public int[] colors1() {
		final CacheObjectConfig c = CacheObjectConfig.load(ctx.bot().getCacheWorker(), id());
		if (c != null && c.originalColors != null) {
			final int[] s = new int[c.originalColors.length];
			for (int i = 0; i < s.length; i++) {
				s[i] = c.originalColors[i];
			}
			return s;
		}
		return new int[0];
	}

	/**
	 * @deprecated Use modifiedColors() instead
	 */
	@Deprecated
	public int[] colors2() {
		final CacheObjectConfig c = CacheObjectConfig.load(ctx.bot().getCacheWorker(), id());
		if (c != null && c.modifiedColors != null) {
			final int[] s = new int[c.modifiedColors.length];
			for (int i = 0; i < s.length; i++) {
				s[i] = c.modifiedColors[i];
			}
			return s;
		}
		return new int[0];
	}

	public short[] originalColors() {
		final CacheObjectConfig c = CacheObjectConfig.load(ctx.bot().getCacheWorker(), id());
		if (c != null && c.originalColors != null) {
			return c.originalColors;
		}
		return new short[0];
	}

	public short[] modifiedColors() {
		final CacheObjectConfig c = CacheObjectConfig.load(ctx.bot().getCacheWorker(), id());
		if (c != null && c.modifiedColors != null) {
			return c.modifiedColors;
		}
		return new short[0];
	}

	public int width() {
		final CacheObjectConfig c = CacheObjectConfig.load(ctx.bot().getCacheWorker(), id());
		if (c != null) {
			return c.xSize;
		}
		return -1;
	}

	public int height() {
		final CacheObjectConfig c = CacheObjectConfig.load(ctx.bot().getCacheWorker(), id());
		if (c != null) {
			return c.ySize;
		}
		return -1;
	}

	public int[] meshIds() {
		final CacheObjectConfig c = CacheObjectConfig.load(ctx.bot().getCacheWorker(), id());
		if (c != null) {
			int[] meshIds = c.meshId;
			if (meshIds == null) {
				meshIds = new int[0];
			}
			return meshIds;
		}
		return new int[0];
	}

	public String[] actions() {
		final CacheObjectConfig c = CacheObjectConfig.load(ctx.bot().getCacheWorker(), id());
		if (c != null) {
			return c.actions;
		}
		return new String[0];
	}

	public int orientation() {
		return object != null ? object.getMeta() >> 6 : 0;
	}

	public Type type() {
		/*
		final BasicObject object = this.object.get();
		return object != null ? object.getMeta() & 0x3f : 0;
		 */
		return type;
	}

	public int relative() {
		final int x, z;
		if (object != null) {
			if (object.isComplex()) {
				x = object.getX();
				z = object.getZ();
			} else {
				final int uid = object.getUid();
				x = (uid & 0x7f) << 7;
				z = ((uid >> 7) & 0x7f) << 7;
			}
		} else {
			x = z = 0;
		}
		return (x << 16) | z;
	}

	@Override
	public boolean valid() {
		return this != ctx.objects.nil()
			&& !(object == null || object.object.isNull())
			&& ctx.objects.select(this, 0).contains(this);
	}

	@Override
	public Tile tile() {
		final Client client = ctx.client();
		final int r = relative();
		final int rx = r >> 16, rz = r & 0xffff;
		if (client != null && rx != 0 && rz != 0) {
			return new Tile(client.getOffsetX() + (rx >> 7), client.getOffsetY() + (rz >> 7), client.getFloor());
		}
		return Tile.NIL;
	}

	@Override
	public Point basePoint() {
		return ctx.game.worldToScreen(localX(), localY(), 0);
	}

	@Override
	public Point centerPoint() {
		// Non-default custom bounds take priority
		final BoundingModel model = boundingModel.get();
		if (model != null && !model.equals(defaultBounds)) {
			return model.centerPoint();
		}

		final Point center = modelCenterPoint();
		if (center != null) {
			return center;
		}
		return basePoint();
	}

	@Override
	public Point nextPoint() {
		// Non-default custom bounds take priority
		final BoundingModel bModel = boundingModel.get();
		if (bModel != null && !bModel.equals(defaultBounds)) {
			return bModel.nextPoint();
		}

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
	public boolean contains(final Point point) {
		// Non-default custom bounds take priority
		final BoundingModel model2 = boundingModel.get();
		if (model2 != null && !model2.equals(defaultBounds)) {
			return model2.contains(point);
		}
		final Model model = model();
		if (model == null || model.nextPoint(localX(), localY()).equals(NIL_POINT)) {
			return model2 != null && model2.contains(point);
		}
		return model.contains(point, localX(), localY());
	}

	@Override
	public String toString() {
		return String.format("%s[id=%d,name=%s,type=%s,tile=%s]", GameObject.class.getName(), id(), name(), type.name(), tile().toString());
	}

	@Override
	public int hashCode() {
		return object != null ? object.hashCode() : 0;
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof GameObject && hashCode() == o.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int localX() {
		if (object.getX() != -1) {
			return object.getX();
		}

		final int r = relative();
		return r >> 16;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int localY() {
		if (object.getZ() != -1) {
			return object.getZ();
		}

		final int r = relative();
		return r & 0xffff;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] modelIds() {
		final CacheObjectConfig c = CacheObjectConfig.load(ctx.bot().getCacheWorker(), id());
		return c != null ? c.meshId : null;
	}

	@Override
	public IRenderable renderable() {
		return object.object.getRenderable();
	}

	public enum Type {
		INTERACTIVE, BOUNDARY, WALL_DECORATION, FLOOR_DECORATION, UNKNOWN
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int modelOrientation() {
		return (object.object.getOrientation());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAnimated() {
		return true;
	}

	public int meta() {
		return object.getMeta();
	}

	@Override
	public Callable<Point> calculateScreenPosition() {
		return ScreenPosition.of(ctx, this);
	}
}
