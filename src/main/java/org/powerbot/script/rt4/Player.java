package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.HashTable;
import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.internal.*;
import org.powerbot.script.Nillable;

import java.awt.*;

/**
 * Player
 */
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
		final IPlayerComposite composite = player != null ? player.getComposite() : null;
		int[] arr = composite != null ? composite.getAppearance() : new int[0];
		if (arr == null) {
			arr = new int[0];
		}
		arr = arr.clone();
		for (int index = 0; index < arr.length; ++index) {
			arr[index] = arr[index] < 512 ? -1 : arr[index] - 512;
		}
		return arr;
	}

	@Override
	public int healthPercent() {
		if (!valid()) {
			return -1;
		}

		if (player == ctx.client().getPlayer()) {
			return ctx.combat.healthPercent();
		}

		return super.healthPercent();
	}

	@Override
	public boolean valid() {
		final IClient client = ctx.client();
		if (client == null || player == null) {
			return false;
		}
		final IPlayer[] arr = client.getPlayers();
		for (final IPlayer a : arr) {
			if (player.equals(a)) {
				return true;
			}
		}
		return false;
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
		final IPlayerComposite composite = player != null ? player.getComposite() : null;
		if (composite == null) {
			return -1;
		}

		return composite.getUid();
	}

	@Override
	public ICache getModelCache() {
		final IClient client = ctx.client();
		if (client == null || player == null) {
			return null;
		}

		return client.getPlayerModelCache();
	}
}
