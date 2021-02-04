package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.client.Client;
import org.powerbot.bot.rt4.client.*;
import org.powerbot.bot.rt4.client.extended.IMobileClient;
import org.powerbot.bot.rt4.client.internal.IActor;
import org.powerbot.bot.rt4.client.internal.ICombatStatus;
import org.powerbot.bot.rt4.client.internal.ICombatStatusData;
import org.powerbot.bot.rt4.client.internal.IRenderable;
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

	protected abstract org.powerbot.bot.rt4.client.Actor getActor();

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
		final org.powerbot.bot.rt4.client.Actor actor = getActor();
		return actor != null ? actor.getAnimation() : -1;
	}

	/**
	 * The speed of the entity.
	 *
	 * @return The current speed.
	 */
	public int speed() {
		final org.powerbot.bot.rt4.client.Actor actor = getActor();
		return actor != null ? actor.getSpeed() : -1;
	}

	/**
	 * The way the entity is facing. 0 for North, 1 for East, 2 for South, 3 for West.
	 *
	 * @return The orientation.
	 */
	public int orientation() {
		final org.powerbot.bot.rt4.client.Actor actor = getActor();
		return actor != null ? actor.getOrientation() / 256 : -1;
	}

	/**
	 * The current message which appears above the head of the entity.
	 *
	 * @return The message.
	 */
	public String overheadMessage() {
		final org.powerbot.bot.rt4.client.Actor actor = getActor();
		final String str = actor != null ? actor.getOverheadMessage() : "";
		return str != null ? str : "";
	}

	/**
	 * Whether or not the entity is currently in motion.
	 *
	 * @return {@code true} if in motion, {@code false} otherwise.
	 */
	public boolean inMotion() {
		return speed() > 0;
	}

	/**
	 * Whether or not the entity has a health bar displayed over their head. This is
	 * used to determine whether or not the entity is currently in combat.
	 *
	 * @return {@code true} if the health bar is visible, {@code false} otherwise.
	 */
	@Deprecated
	public boolean inCombat() {
		return healthBarVisible();
	}

	/**
	 * Whether or not the entity has a health bar displayed over their head. This can be
	 * used to determine whether or not the entity is currently in combat.
	 *
	 * @return {@code true} if the health bar is visible, {@code false} otherwise.
	 */
	public boolean healthBarVisible() {
		final Client client = ctx.client();
		if (client == null) {
			return false;
		}

		if (client.isMobile()) {
			return ((IMobileClient) client.get()).isHealthBarVisible((IActor) getActor().get());
		} else {
			final CombatStatusData[] data = getBarData();
			return data != null && data[1] != null && data[1].getCycleEnd() < client.getCycle();
		}
	}

	/**
	 * The current percent of the health bar.
	 *
	 * @return The percentage of the health bar (0-100).
	 */
	public int healthPercent() {
		if (!valid()) {
			return -1;
		}

		final Client client = ctx.client();
		if (client == null) {
			return -1;
		}

		if (client.isMobile()) {
			return ((IMobileClient) client.get()).getHealthPercent((IActor) getActor().get());
		} else {
			final CombatStatusData[] data = getBarData();
			if (data == null || data[1] == null) {
				return 100;
			}
			if (getBarComponent() == null) {
				return 100;
			}
			return (int) Math.ceil(data[1].getHealthRatio() * 100d / getBarComponent().getWidth());
		}
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
	public Actor interacting() {
		final Actor nil = ctx.npcs.nil();
		final org.powerbot.bot.rt4.client.Actor actor = getActor();
		final int index = actor != null ? actor.getInteractingIndex() : -1;
		if (index == -1) {
			return nil;
		}
		final Client client = ctx.client();
		if (client == null) {
			return nil;
		}
		if (index < 32768) {
			final org.powerbot.bot.rt4.client.Npc[] npcs = client.getNpcs();
			return index >= 0 && index < npcs.length ? new Npc(ctx, npcs[index]) : nil;
		} else {
			final int pos = index - 32768;
			if (pos == client.getPlayerIndex()) {
				return new Player(ctx, client.getPlayer());
			}
			final org.powerbot.bot.rt4.client.Player[] players = client.getPlayers();
			return pos < players.length ? new Player(ctx, players[pos]) : nil;
		}
	}

	public int relative() {
		final org.powerbot.bot.rt4.client.Actor actor = getActor();
		final int x, z;
		if (actor != null) {
			x = actor.getX();
			z = actor.getZ();
		} else {
			x = z = 0;
		}
		return (x << 16) | z;
	}

	@Override
	public Tile tile() {
		final Client client = ctx.client();
		final org.powerbot.bot.rt4.client.Actor actor = getActor();
		if (client != null && actor != null) {
			return new Tile(client.getOffsetX() + (actor.getX() >> 7), client.getOffsetY() + (actor.getZ() >> 7), client.getFloor());
		}
		return Tile.NIL;
	}

	public Point basePoint() {
		final org.powerbot.bot.rt4.client.Actor actor = getActor();

		if (actor != null) {
			return ctx.game.worldToScreen(localX(), localY(), (actor.getHeight() / 2));
		}
		return NIL_POINT;
	}

	public int getHeight() {
		final org.powerbot.bot.rt4.client.Actor actor = getActor();
		if (actor != null) {
			return actor.getHeight();
		}
		return 0;
	}

	@Override
	public Point nextPoint() {
		final org.powerbot.bot.rt4.client.Actor actor = getActor();
		if (actor == null) {
			return NIL_POINT;
		}
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
	public Point centerPoint() {
		final org.powerbot.bot.rt4.client.Actor actor = getActor();
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

		final Point center = modelCenterPoint();
		if (center != null) {
			return center;
		}

		return basePoint();
	}

	@Override
	public boolean contains(final Point point) {
		final org.powerbot.bot.rt4.client.Actor actor = getActor();
		if (actor == null) {
			return false;
		}
		// Non-default custom bounds take priority
		final BoundingModel bModel = boundingModel.get();
		if (bModel != null && !bModel.equals(defaultBounds)) {
			return bModel.contains(point);
		}

		final Model model = model();
		if (model == null || model.nextPoint(localX(), localY()).equals(NIL_POINT)) {
			return bModel != null && bModel.contains(point);
		}
		return model.contains(point, localX(), localY());
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || !Actor.class.isAssignableFrom(o.getClass())) {
			return false;
		}
		final org.powerbot.bot.rt4.client.Actor actor = ((Actor) o).getActor();
		return actor != null && actor.equals(getActor());
	}

	@Override
	public int hashCode() {
		final org.powerbot.bot.rt4.client.Actor actor = getActor();
		return actor != null ? actor.hashCode() : 0;
	}

	private CombatStatus[] getBarNodes() {
		final org.powerbot.bot.rt4.client.Actor accessor = getActor();
		if (accessor == null) {
			return null;
		}
		final org.powerbot.bot.rt4.client.LinkedList barList = accessor.getCombatStatusList();
		if (barList == null) {
			return null;
		}
		final Node tail = barList.getSentinel();
		if (tail == null) {
			return null;
		}
		final CombatStatus health;
		final CombatStatus secondary;
		final Node current;
		current = tail.getNext();
		if (current.getNext().getNodeId() != tail.getNodeId()) {
			if (ICombatStatus.class.isAssignableFrom(current.get().getClass())) {
				secondary = new CombatStatus((ICombatStatus) current.get());
			} else {
				secondary = null;
			}
			if (ICombatStatus.class.isAssignableFrom(current.getNext().get().getClass())) {
				health = new CombatStatus((ICombatStatus) current.getNext().get());
			} else {
				health = null;
			}
		} else {
			secondary = null;
			if (ICombatStatus.class.isAssignableFrom(current.get().getClass())) {
				health = new CombatStatus((ICombatStatus) current.get());
			} else {
				health = null;
			}
		}
		return new CombatStatus[]{secondary, health};
	}

	private BarComponent getBarComponent() {
		final CombatStatus[] nodes = getBarNodes();
		final Client client = ctx.client();
		if (nodes == null || client == null) {
			return null;
		}
		for (CombatStatus node : nodes) {
			if (node == null || node.isNull() ||
				!node.isTypeOf(CombatStatus.class)) {
				continue;
			}

			final CombatStatus status = new CombatStatus(node.get());

			return status.getBarComponent();
		}
		return null;
	}

	private CombatStatusData[] getBarData() {
		final CombatStatus[] nodes = getBarNodes();
		final Client client = ctx.client();
		if (nodes == null || client == null) {
			return null;
		}
		final CombatStatusData[] data = new CombatStatusData[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] == null || nodes[i].isNull()) {
				data[i] = null;
				continue;
			}
			final CombatStatus status = nodes[i];

			final org.powerbot.bot.rt4.client.LinkedList statuses;
			try {
				statuses = status.getList();
			} catch (final IllegalArgumentException ignored) {
				continue;
			}
			if (statuses == null) {
				data[i] = null;
				continue;
			}
			final Node<ICombatStatus> node = statuses.getSentinel().getNext();
			if (node.isNull() || !node.isTypeOf(CombatStatusData.class)) {
				data[i] = null;
				continue;
			}
			data[i] = new CombatStatusData((ICombatStatusData) node.get());
		}
		return data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int localX() {
		final org.powerbot.bot.rt4.client.Actor actor = getActor();

		return actor != null ? getActor().getX() : 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int localY() {
		final org.powerbot.bot.rt4.client.Actor actor = getActor();

		return actor != null ? actor.getZ() : 0;
	}

	@Override
	public IRenderable renderable() {
		final org.powerbot.bot.rt4.client.Actor actor = getActor();

		return actor != null ? (IRenderable) actor.get() : null;
	}

	@Override
	public int modelOrientation() {
		final org.powerbot.bot.rt4.client.Actor actor = getActor();

		return actor != null ? (actor.getOrientation() + 512) & 0x3FFF : 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAnimated() {
		return true;
	}

	@Override
	public Callable<Point> calculateScreenPosition() {
		return ScreenPosition.of(ctx, this);
	}
}
