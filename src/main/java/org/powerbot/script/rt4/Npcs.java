package org.powerbot.script.rt4;

import org.powbot.stream.locatable.interactive.NpcStream;
import org.powbot.stream.Streamable;
import org.powerbot.bot.rt4.client.internal.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Npcs
 */
public class Npcs extends BasicQuery<Npc> implements Streamable<NpcStream> {
	public Npcs(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Npc> get() {
		return getInternal().stream().map(n -> new Npc(ctx, n)).collect(Collectors.toList());
	}

	private List<INpc> getInternal() {
		final List<INpc> r = new ArrayList<>();
		final IClient client = ctx.client();
		if (client == null) {
			return r;
		}
		final int[] indices = client.getNpcIndices();
		final INpc[] npcs = client.getNpcs();
		if (indices == null || npcs == null) {
			return r;
		}
		for (int index = 0; index < Math.min(client.getNpcCount(), indices.length); ++index) {
			final INpc n = npcs[indices[index]];
			if (n != null) {
				r.add(n);
			}
		}
		return r;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NpcStream toStream() {
		return new NpcStream(ctx, getInternal().stream());
	}

	@Override
	public Npc nil() {
		return Npc.NIL;
	}


	public static final INpc NIL = new INpc() {

		@Override
		public boolean isNil() {
			return true;
		}

		@Override
		public int id() {
			return -1;
		}

		@Override
		public boolean valid() {
			return false;
		}

		@Override
		public Callable<Point> calculateScreenPosition() {
			return null;
		}

		@Override
		public IModel getModel() {
			return null;
		}

		@Override
		public INpcConfig getConfig() {
			return null;
		}

		@Override
		public INpc nil() {
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
	};
}
