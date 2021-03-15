package org.powerbot.script.rt4;

import org.powbot.stream.locatable.interactive.PlayerStream;
import org.powbot.stream.Streamable;
import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.internal.IPlayer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Players
 */
public class Players extends PlayerQuery<Player> implements Streamable<PlayerStream> {
	public Players(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Player> get() {
		final List<Player> r = new CopyOnWriteArrayList<>();
		final IClient client = ctx.client();
		if (client == null) {
			return r;
		}
		final int[] indices = client.getPlayerIndices();
		final IPlayer[] players = client.getPlayers();
		if (indices == null || players == null) {
			return r;
		}
		for (int index = 0; index < Math.min(client.getPlayerCount(), indices.length); index++) {
			final int k = indices[index];
			final IPlayer p = players[k];
			if (p != null) {
				r.add(new Player(ctx, p));
			}
		}
		return r;
	}

	public Player local() {
		final Player r = new Player(ctx, null);
		final IClient client = ctx.client();
		if (client == null) {
			return r;
		}
		return new Player(ctx, client.getPlayer());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerStream toStream() {
		return new PlayerStream(ctx, get().stream());
	}

	@Override
	public Player nil() {
		return Player.NIL;
	}
}
