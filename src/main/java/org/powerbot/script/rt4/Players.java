package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.client.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Players
 */
public class Players extends PlayerQuery<Player> {
	public Players(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Player> get() {
		final List<Player> r = new CopyOnWriteArrayList<>();
		final Client client = ctx.client();
		if (client == null) {
			return r;
		}
		final int[] indices = client.getPlayerIndices();
		final org.powerbot.bot.rt4.client.Player[] players = client.getPlayers();
		if (indices == null || players == null) {
			return r;
		}
		for (int index = 0; index < Math.min(client.getPlayerCount(), indices.length); index++) {
			final int k = indices[index];
			final org.powerbot.bot.rt4.client.Player p = players[k];
			if (!p.isNull()) {
				r.add(new Player(ctx, p));
			}
		}
		return r;
	}

	public Player local() {
		final Player r = new Player(ctx, null);
		final Client client = ctx.client();
		if (client == null) {
			return r;
		}
		return new Player(ctx, client.getPlayer());
	}

	@Override
	public Player nil() {
		return new Player(ctx, null);
	}
}
