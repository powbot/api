package org.powerbot.script.rt4;

import org.powbot.stream.locatable.interactive.PlayerStream;
import org.powbot.stream.Streamable;
import org.powerbot.bot.rt4.client.internal.*;

import java.awt.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

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
		return getInternal().stream().map(p -> new Player(ctx, p)).collect(Collectors.toList());
	}

	public List<IPlayer> getInternal() {
		final List<IPlayer> r = new CopyOnWriteArrayList<>();
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
				r.add(p);
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
		return new PlayerStream(ctx, getInternal().stream());
	}

	@Override
	public Player nil() {
		return Player.NIL;
	}

	public static final IPlayer NIL = new IPlayer() {
		@Override
		public int getCombatLevel() {
			return 0;
		}

		@Override
		public IPlayerComposite getComposite() {
			return null;
		}

		@Override
		public IStringRecord getName() {
			return null;
		}

		@Override
		public int getTeam() {
			return 0;
		}

		@Override
		public int getAnimation() {
			return 0;
		}

		@Override
		public ILinkedList getCombatStatusList() {
			return null;
		}

		@Override
		public int getHeight() {
			return 0;
		}

		@Override
		public int getInteractingIndex() {
			return 0;
		}

		@Override
		public int getOrientation() {
			return 0;
		}

		@Override
		public String getOverheadMessage() {
			return null;
		}

		@Override
		public int getSpeed() {
			return 0;
		}

		@Override
		public int getX() {
			return 0;
		}

		@Override
		public int getZ() {
			return 0;
		}

		@Override
		public IModel getModel() {
			return null;
		}

		@Override
		public IEntry getNext() {
			return null;
		}

		@Override
		public long getNodeId() {
			return 0;
		}

		@Override
		public Callable<Point> calculateScreenPosition() {
			return null;
		}
	};
}
