package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.internal.*;
import org.powerbot.script.*;
import org.powerbot.script.action.Emittable;
import org.powerbot.script.action.ObjectAction;
import org.powerbot.util.ScreenPosition;

import java.awt.*;
import java.util.concurrent.Callable;

/**
 * GameObject
 */
@Deprecated
public class GameObject extends Interactive implements Nameable, InteractiveEntity, Identifiable, Validatable,
	Actionable, Modelable, Nillable<GameObject>, Emittable<ObjectAction> {

	public static final Color TARGET_COLOR = new Color(0, 255, 0, 20);
	public static final int[] lookup;
	public static final GameObject NIL = new GameObject(org.powerbot.script.ClientContext.ctx(), null, Type.UNKNOWN);

	static {
		lookup = new int[32];
		int i = 2;
		for (int j = 0; j < 32; j++) {
			lookup[j] = i - 1;
			i += i;
		}
	}

	public static Type getType(IBasicObject obj) {
		if (obj instanceof IBoundaryObject) {
			return Type.BOUNDARY;
		} else if (obj instanceof IFloorObject) {
			return Type.FLOOR_DECORATION;
		} else if (obj instanceof IGameObject) {
			return Type.INTERACTIVE;
		} else if (obj instanceof IWallObject) {
			return Type.WALL_DECORATION;
		}

		return Type.UNKNOWN;
	}

	private final IBasicObject object;
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

	public GameObject(final ClientContext ctx, final IBasicObject object, final Type type) {
		super(ctx);
		this.object = object;
		this.type = type;
		boundingModel.set(defaultBounds);
		setInteractive(object);
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
		return object != null ? object.mainId() : -1;
	}

	@Override
	public int id() {
		return object != null ? object.id() : -1;
	}

	@Override
	public String name() {
		return object != null ? object.name() : "";
	}

	public short[] originalColors() {
		return object != null ? object.originalColors() : new short[0];
	}

	public short[] modifiedColors() {
		return object != null ? object.modifiedColors() : new short[0];
	}

	public int width() {
		return object != null ? object.width() : -1;
	}

	public int height() {
		return object != null ? object.height() : -1;
	}

	public int[] meshIds() {
		return object != null ? object.meshIds() : new int[0];
	}

	public String[] actions() {
		return object != null ? object.actions() : new String[0];
	}

	public int orientation() {
		return object != null ? object.orientation() : -1;
	}

	public Type type() {
		/*
		final BasicObject object = this.object.get();
		return object != null ? object.getMeta() & 0x3f : 0;
		 */
		return type;
	}

	public int relative() {
		return object != null ? object.relative() : -1;
	}

	@Override
	public boolean valid() {
		return this != NIL
			&& object != null
			&& ctx.objects.select(this, 0).contains(this);
	}

	@Override
	public Tile tile() {
		return object != null ? object.tile() : Tile.NIL;
	}

	@Override
	public Point basePoint() {
		return object != null ? object.basePoint() : NIL_POINT;
	}

	@Override
	public Point centerPoint() {
		// Non-default custom bounds take priority
		final BoundingModel model = boundingModel.get();
		if (model != null && !model.equals(defaultBounds)) {
			return model.centerPoint();
		}

		return object != null ? object.centerPoint() : NIL_POINT;
	}

	@Override
	public Point nextPoint() {
		// Non-default custom bounds take priority
		final BoundingModel bModel = boundingModel.get();
		if (bModel != null && !bModel.equals(defaultBounds)) {
			return bModel.nextPoint();
		}

		return object != null ? object.nextPoint() : NIL_POINT;
	}

	@Override
	public boolean contains(final Point point) {
		// Non-default custom bounds take priority
		final BoundingModel model2 = boundingModel.get();
		if (model2 != null && !model2.equals(defaultBounds)) {
			return model2.contains(point);
		}

		return object != null && object.contains(point);
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
		return object != null ? object.localX() : -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int localY() {
		return object != null ? object.localY() : -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] modelIds() {
		return object != null ? object.modelIds() : new int[0];
	}

	@Override
	public IRenderable[] renderables() {
		return object.getRenderables();
	}

	@Override
	public int[] modelOrientations() {
		return object.modelOrientations();
	}

	@Override
	public GameObject nil() {
		return NIL;
	}

	@Override
	public ObjectAction createAction(String action, boolean async) {
		return object != null ? object.createAction(action, async) : null;
	}

	public enum Type {
		INTERACTIVE, BOUNDARY, WALL_DECORATION, FLOOR_DECORATION, UNKNOWN
	}

	/**
	 * Returns false, don't use
	 *
	 * @return boolean
	 */
	@Deprecated
	public boolean isAnimated() {
		return false;
	}

	public int meta() {
		return object.getMeta();
	}

	@Override
	public Callable<Point> calculateScreenPosition() {
		return object != null ? object.calculateScreenPosition() : () -> NIL_POINT;
	}

	@Override
	public long getModelCacheId() {
		// This id doesn't seem to be right?
		return object != null ? object.getModelCacheId() : -1;
	}

	@Override
	public ICache getModelCache() {
		return object != null ? object.getModelCache() : null;
	}
}
