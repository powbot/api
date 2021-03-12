package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.HashTable;
import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.internal.*;
import org.powerbot.script.Nillable;

import java.awt.*;

/**
 * Player
 */
@Deprecated
public class Player extends Actor implements Nillable<Player> {
	public static final Color TARGET_COLOR = new Color(255, 0, 0, 15);
	public static final Player NIL = new Player(org.powerbot.script.ClientContext.ctx(), null);
	private final IPlayer player;

	public Player(final ClientContext ctx, final IPlayer player) {
		super(ctx);
		this.player = player;
	}

	@Override
	protected IActor getActor() {
		return player;
	}

	@Override
	public String name() {
		return player != null ? player.name() : "";
	}

	@Override
	public int combatLevel() {
		return player != null ? player.getCombatLevel() : -1;
	}

	public int team() {
		return player != null ? player.getTeam() : -1;
	}

	public int[] appearance() {
		return player != null ? player.appearance() : new int[0];
	}

	@Override
	public int healthPercent() {
		if (!valid()) {
			return -1;
		}

		return player != null ? player.healthPercent() : -1;
	}

	@Override
	public boolean valid() {
		return player != null && player.valid();
	}

	@Override
	public String toString() {
		return String.format("%s[name=%s/level=%d/team=%d]",
				Player.class.getName(), name(), combatLevel(), team());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int[] modelIds() {
		return player != null ? player.modelIds() : null;
	}

	@Override
	public Player nil() {
		return NIL;
  }
  
	public long getModelCacheId() {
		return player != null ? player.getModelCacheId() : -1;
	}

	@Override
	public ICache getModelCache() {
		return player != null ? player.getModelCache() : null;
	}
}
