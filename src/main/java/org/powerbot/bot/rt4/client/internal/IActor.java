package org.powerbot.bot.rt4.client.internal;

import org.powerbot.bot.rt4.client.extended.IMobileClient;
import org.powerbot.script.*;
import org.powerbot.script.ClientContext;
import org.powerbot.script.Interactive;
import org.powerbot.script.rt4.*;

import java.awt.*;

import static org.powerbot.script.rt4.Interactive.NIL_POINT;

public interface IActor extends IRenderable, Viewable, Interactive, Modelable,
	Identifiable, Nameable, InteractiveEntity, Actionable {

	int getAnimation();

	ILinkedList getCombatStatusList();

	int getHeight();

	int getInteractingIndex();

	int getOrientation();

	String getOverheadMessage();

	int getSpeed();

	int getX();

	int getZ();
	
	int getCombatLevel();

	default int combatLevel() {
		return getCombatLevel();
	}

	/**
	 * Whether or not the entity has a health bar displayed over their head. This is
	 * used to determine whether or not the entity is currently in combat.
	 *
	 * @return {@code true} if the health bar is visible, {@code false} otherwise.
	 */
	@Deprecated
	default boolean inCombat() {
		return healthBarVisible();
	}

	/**
	 * Whether or not the entity has a health bar displayed over their head. This can be
	 * used to determine whether or not the entity is currently in combat.
	 *
	 * @return {@code true} if the health bar is visible, {@code false} otherwise.
	 */
	default boolean healthBarVisible() {
		final IClient client = ctx().client();
		if (client == null) {
			return false;
		}

		if (client.isMobile()) {
			return ((IMobileClient) client).isHealthBarVisible(this);
		} else {
			final ICombatStatusData[] data = getBarData();
			return data != null && data[1] != null && data[1].getCycleEnd() < client.getCycle();
		}
	}

	/**
	 * The current percent of the health bar.
	 *
	 * @return The percentage of the health bar (0-100).
	 */
	default int healthPercent() {
		if (!valid()) {
			return -1;
		}

		final IClient client = ClientContext.ctx().client();
		if (client == null) {
			return -1;
		}

		if (client.isMobile()) {
			return ((IMobileClient) client).getHealthPercent(this);
		} else {
			final ICombatStatusData[] data = getBarData();
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
	 * The current entity of which this entity is interacting with. If
	 * the client is not loaded or there is no entity being interacted with,
	 * this will return {@link Npcs#nil()}.
	 *
	 * @return The entity of which is being interacted with.
	 */
	default IActor interacting() {
		final Actor nil = ctx().npcs.nil();
		final int index = getInteractingIndex();
		if (index == -1) {
			return Npcs.NIL;
		}
		final IClient client = ctx().client();
		if (client == null) {
			return Npcs.NIL;
		}
		if (index < 32768) {
			final INpc[] npcs = client.getNpcs();
			return index >= 0 && index < npcs.length ? npcs[index] : Npcs.NIL;
		} else {
			final int pos = index - 32768;
			if (pos == client.getPlayerIndex()) {
				return client.getPlayer();
			}
			final IPlayer[] players = client.getPlayers();
			return pos < players.length ? players[pos] : Players.NIL;
		}
	}

	private ICombatStatus[] getBarNodes() {
		final ILinkedList barList = getCombatStatusList();
		if (barList == null) {
			return null;
		}
		final INode tail = barList.getSentinel();
		if (tail == null) {
			return null;
		}
		final ICombatStatus health;
		final ICombatStatus secondary;
		final INode current;
		current = tail.getNext();
		if (current.getNext().getNodeId() != tail.getNodeId()) {
			if (ICombatStatus.class.isAssignableFrom(current.getClass())) {
				secondary = (ICombatStatus) current;
			} else {
				secondary = null;
			}
			if (ICombatStatus.class.isAssignableFrom(current.getNext().getClass())) {
				health = (ICombatStatus) current.getNext();
			} else {
				health = null;
			}
		} else {
			secondary = null;
			if (ICombatStatus.class.isAssignableFrom(current.getClass())) {
				health = (ICombatStatus) current;
			} else {
				health = null;
			}
		}
		return new ICombatStatus[]{secondary, health};
	}

	private IBarComponent getBarComponent() {
		final ICombatStatus[] nodes = getBarNodes();
		final IClient client = ctx().client();
		if (nodes == null || client == null) {
			return null;
		}
		for (ICombatStatus node : nodes) {
			if (node == null) {
				continue;
			}

			return node.getBarComponent();
		}
		return null;
	}

	private ICombatStatusData[] getBarData() {
		final ICombatStatus[] nodes = getBarNodes();
		final IClient client = ctx().client();
		if (nodes == null || client == null) {
			return null;
		}
		final ICombatStatusData[] data = new ICombatStatusData[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] == null) {
				data[i] = null;
				continue;
			}
			final ICombatStatus status = nodes[i];

			final ILinkedList statuses;
			try {
				statuses = status.getList();
			} catch (final IllegalArgumentException ignored) {
				continue;
			}
			if (statuses == null) {
				data[i] = null;
				continue;
			}
			final INode node = statuses.getSentinel().getNext();
			if (!(node instanceof ICombatStatusData)) {
				data[i] = null;
				continue;
			}
			data[i] = (ICombatStatusData) node;
		}
		return data;
	}


	/**
	 * Whether or not the entity is currently in motion.
	 *
	 * @return {@code true} if in motion, {@code false} otherwise.
	 */
	default boolean inMotion() {
		return getSpeed() > 0;
	}

	default int orientation() {
		return getOrientation() / 256;
	}

	default int relative() {
		final int x = getX();
		final int z = getZ();
		return (x << 16) | z;
	}

	@Override
	default boolean inViewport() {
		return ClientContext.ctx().game.inViewport(basePoint()) || ClientContext.ctx().game.inViewport(centerPoint());
	}

	@Override
	default void draw(Graphics render) {
		drawModel(render);
	}

	@Override
	default void draw(Graphics render, int alpha) {
		drawModel(render);
	}

	@Override
	default Point basePoint() {
		return ctx().game.worldToScreen(localX(), localY(), (getHeight() / 2));
	}

	@Override
	default Point centerPoint() {
		final Point center = modelCenterPoint();
		if (center != null) {
			return center;
		}

		return basePoint();
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
	default int localX() {
		return getX();
	}

	@Override
	default int localY() {
		return getZ();
	}

	@Override
	default int[] modelOrientations() {
		return new int[]{getOrientation() & 0x3FFF};
	}

	@Override
	default org.powerbot.script.rt4.ClientContext ctx() {
		return org.powerbot.script.rt4.ClientContext.ctx();
	}

	@Override
	default IRenderable[] renderables() {
		return new IRenderable[]{this};
	}

	@Override
	default long getModelCacheId() {
		return id();
	}

	@Override
	default ICache getModelCache() {
		final IClient client = ctx().client();
		if (client == null) {
			return null;
		}

		return client.getNpcModelCache();
	}

	@Override
	default Tile tile() {
		final IClient client = ClientContext.ctx().client();
		if (client != null) {
			return new Tile(client.getOffsetX() + (getX() >> 7), client.getOffsetY() + (getZ() >> 7), client.getFloor());
		}

		return Tile.NIL;
	}

	/**
	 * The current message which appears above the head of the entity.
	 *
	 * @return The message.
	 */
	default String overheadMessage() {
		final String str = getOverheadMessage();
		return str != null ? str : "";
	}

	default int animation() {
		return getAnimation();
	}

}
