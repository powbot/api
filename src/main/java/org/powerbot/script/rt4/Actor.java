package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.extended.IMobileClient;
import org.powerbot.bot.rt4.client.internal.*;
import org.powerbot.script.Client;
import org.powerbot.script.Tile;
import org.powerbot.script.*;
import org.powerbot.util.ScreenPosition;

import java.awt.*;
import java.util.concurrent.Callable;

/**
 * Actor
 * A base class of all characters within Runescape.
 */
public abstract class Actor extends Interactive implements InteractiveEntity, Nameable, Validatable, Modelable {

	private final BoundingModel defaultBounds = new BoundingModel(ctx, -32, 32, -192, 0, -32, 32) {
		@Override
		public int x() {
			return relative() >> 16;
		}

		@Override
		public int z() {
			return relative() & 0xffff;
		}
	};

	Actor(final ClientContext ctx) {
		super(ctx);
		boundingModel.set(defaultBounds);
		setInteractive(getActor());
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

	protected abstract IActor getActor();

	/**
	 * The name of the entity.
	 *
	 * @return The name.
	 */
	public abstract String name();

	/**
	 * The combat level of the entity.
	 *
	 * @return The combat level.
	 */
	public abstract int combatLevel();

	/**
	 * The current animation being enacted by the entity, or {@code -1}
	 * if no animation is occurring.
	 *
	 * @return The animation ID.
	 */
	public int animation() {
		final IActor actor = getActor();
		return actor != null ? actor.getAnimation() : -1;
	}

	/**
	 * Whether or not the entity has a health bar displayed over their head. This can be
	 * used to determine whether or not the entity is currently in combat.
	 *
	 * @return {@code true} if the health bar is visible, {@code false} otherwise.
	 */
	public boolean healthBarVisible() {
		final IActor actor = getActor();

		return actor != null && actor.healthBarVisible();
	}
	/**
	 * The speed of the entity.
	 *
	 * @return The current speed.
	 */
	public int speed() {
		final IActor actor = getActor();
		return actor != null ? actor.getSpeed() : -1;
	}

	/**
	 * The way the entity is facing. 0 for North, 1 for East, 2 for South, 3 for West.
	 *
	 * @return The orientation.
	 */
	public int orientation() {
		final IActor actor = getActor();
		return actor != null ? actor.orientation() : -1;
	}

	/**
	 * The current message which appears above the head of the entity.
	 *
	 * @return The message.
	 */
	public String overheadMessage() {
		final IActor actor = getActor();
		return actor != null ? actor.overheadMessage() : "";
	}

	/**
	 * Whether or not the entity is currently in motion.
	 *
	 * @return {@code true} if in motion, {@code false} otherwise.
	 */
	public boolean inMotion() {
		final IActor actor = getActor();

		return actor == null || actor.inMotion();
	}

	/**
	 * Whether or not the entity has a health bar displayed over their head. This is
	 * used to determine whether or not the entity is currently in combat.
	 *
	 * @return {@code true} if the health bar is visible, {@code false} otherwise.
	 */
	@Deprecated
	public boolean inCombat() {
		final IActor actor = getActor();
		return actor == null || actor.inCombat();
	}

	/**
	 * The current percent of the health bar.
	 *
	 * @return The percentage of the health bar (0-100).
	 */
	public int healthPercent() {
		final IActor actor = getActor();

		return actor != null ? actor.healthPercent() : -1;
	}

	/**
	 * The health of the entity.
	 *
	 * @return The health of the entity.
	 * @deprecated This was deprecated as the client no longer receives
	 * the absolute health value of the entity. This will now return
	 * {@link Actor#healthPercent()} instead.
	 */
	@Deprecated
	public int health() {
		return healthPercent();
	}

	/**
	 * The maximum health of the entity.
	 *
	 * @return {@code 100}
	 * @deprecated This was deprecated as the client no longer
	 * receives the absolute health value of the entity. This will
	 * now return {@code 100} instead, rendering it useless. This
	 * function is only kept for backwards compatibility, and should
	 * not be used.
	 */
	@Deprecated
	public int maxHealth() {
		return 100;
	}

	/**
	 * The current entity of which this entity is interacting with. If
	 * the client is not loaded or there is no entity being interacted with,
	 * this will return {@link Npcs#nil()}.
	 *
	 * @return The entity of which is being interacted with.
	 */
	public IActor interacting() {
		final IActor actor = getActor();
		return actor != null ? actor.interacting() : Npcs.NIL;
	}

	public int relative() {
		final IActor actor = getActor();
		return actor != null ? actor.relative() : -1;
	}

	@Override
	public Tile tile() {
		final IActor actor = getActor();

		return actor != null ? actor.tile() : Tile.NIL;
	}

	@Override
	public Point basePoint() {
		final IActor actor = getActor();

		return actor != null ? actor.basePoint() : NIL_POINT;
	}

	public int getHeight() {
		final IActor actor = getActor();
		return actor != null ? actor.getHeight() : -1;
	}

	@Override
	public Point nextPoint() {
		final IActor actor = getActor();
		if (actor == null) {
			return NIL_POINT;
		}
		// Non-default custom bounds take priority
		final BoundingModel bModel = boundingModel.get();
		if (bModel != null && !bModel.equals(defaultBounds)) {
			return bModel.nextPoint();
		}

		return actor.nextPoint();
	}

	@Override
	public Point centerPoint() {
		final IActor actor = getActor();
		if (actor == null) {
			return NIL_POINT;
		}
		// Non-default custom bounds take priority
		final BoundingModel bModel = boundingModel.get();
		if (bModel != null && !bModel.equals(defaultBounds)) {
			final Point center = bModel.centerPoint();
			if (center != null) {
				return center;
			}
		}

		return actor.centerPoint();
	}

	@Override
	public boolean contains(final Point point) {
		final IActor actor = getActor();
		if (actor == null) {
			return false;
		}
		// Non-default custom bounds take priority
		final BoundingModel bModel = boundingModel.get();
		if (bModel != null && !bModel.equals(defaultBounds)) {
			return bModel.contains(point);
		}

		return actor.contains(point);
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || !Actor.class.isAssignableFrom(o.getClass())) {
			return false;
		}
		final IActor actor = ((Actor) o).getActor();
		return actor != null && actor.equals(getActor());
	}

	@Override
	public int hashCode() {
		final IActor actor = getActor();
		return actor != null ? actor.hashCode() : 0;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int localX() {
		final IActor actor = getActor();

		return actor != null ? getActor().localX() : 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int localY() {
		final IActor actor = getActor();

		return actor != null ? actor.localY() : 0;
	}

	@Override
	public IRenderable[] renderables() {
		final IActor actor = getActor();

		return actor != null ? actor.renderables() : new IRenderable[0];
	}

	@Override
	public int[] modelOrientations() {
		final IActor actor = getActor();

		return actor != null ? actor.modelOrientations() : new int[0];
	}

	/**
	 * Whether or not the actor is animating (animation() != -1)
	 *
	 * @return boolean
	 */
	@Deprecated
	public boolean isAnimated() {
		return animation() != -1;
	}

	@Override
	public Callable<Point> calculateScreenPosition() {
		final IActor actor = getActor();
		if (actor != null) {
			return actor.calculateScreenPosition();
		}

		return () -> NIL_POINT;
	}
}
