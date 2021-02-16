package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.HashTable;
import org.powerbot.bot.rt4.client.*;
import org.powerbot.script.Nillable;
import org.powerbot.bot.rt4.client.internal.IModel;
import org.powerbot.bot.rt4.client.internal.INode;

import java.awt.*;

/**
 * Player
 */
public class Player extends Actor implements Nillable<Player> {
	public static final Color TARGET_COLOR = new Color(255, 0, 0, 15);
	public static final Player NIL = new Player(org.powerbot.script.ClientContext.ctx(), null);
	private final org.powerbot.bot.rt4.client.Player player;

	Player(final ClientContext ctx, final org.powerbot.bot.rt4.client.Player player) {
		super(ctx);
		this.player = player;
	}

	@Override
	protected org.powerbot.bot.rt4.client.Actor getActor() {
		return player;
	}

	@Override
	public String name() {
		final String str = player != null ? player.getName() : "";
		return str != null ? str : "";
	}

	@Override
	public int combatLevel() {
		return player != null ? player.getCombatLevel() : -1;
	}

	public int team() {
		return player != null ? player.getTeam() : -1;
	}

	public int[] appearance() {
		final PlayerComposite composite = player != null ? player.getComposite() : null;
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
		final Client client = ctx.client();
		if (client == null || player == null) {
			return false;
		}
		final org.powerbot.bot.rt4.client.Player[] arr = client.getPlayers();
		for (final org.powerbot.bot.rt4.client.Player a : arr) {
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
		return null;
	}

	@Override
	public Player nil() {
		return NIL;
  }
  
	public long getModelCacheId() {
		final PlayerComposite composite = player != null ? player.getComposite() : null;
		if (composite == null) {
			return -1;
		}

		return composite.getUid();
	}

	@Override
	public Cache getModelCache() {
		final Client client = ctx.client();
		if (client == null || player == null) {
			return null;
		}

		return client.getPlayerModelCache();
	}
}
