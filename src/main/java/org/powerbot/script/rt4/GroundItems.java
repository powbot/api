package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.*;
import org.powerbot.bot.rt4.client.*;
import org.powerbot.script.Tile;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GroundItems
 */
public class GroundItems extends BasicQuery<GroundItem> {
	public GroundItems(final ClientContext ctx) {
		super(ctx);
	}

	public BasicQuery<GroundItem> select(final int radius) {
		final Client client = ctx.client();
		return select(get(radius, client != null ? client.getFloor() : -1));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GroundItem> get() {
		final Client client = ctx.client();
		return get(104, client != null ? client.getFloor() : -1);
	}

	private List<GroundItem> get(int radius, final int floor) {
		if (radius < 1) {
			radius = 110;
		}
		final List<GroundItem> r = new CopyOnWriteArrayList<>();
		final Client client = ctx.client();
		final NodeDeque[][][] dequeArray;
		if (client == null || (dequeArray = client.getGroundItems()) == null) {
			return r;
		}
		final NodeDeque[][] rows;
		if (floor > -1 && floor < dequeArray.length) {
			rows = dequeArray[floor];
		} else {
			rows = null;
		}
		if (rows == null) {
			return r;
		}
		final List<GroundItem> list = new LinkedList<>();
		final Tile tile = new Tile(client.getOffsetX(), client.getOffsetY(), floor);
		final Tile ct = ctx.players.local().tile().derive(-tile.x(), -tile.y());
		for (int x = Math.max(0, ct.x() - radius); x < Math.min(rows.length, ct.x() + radius + 1); x++) {
			final NodeDeque[] row = rows[x];
			if (row == null) {
				continue;
			}
			for (int y = Math.max(0, ct.y() - radius); y < Math.min(row.length, ct.y() + radius + 1); y++) {
				for (final ItemNode n : NodeQueue.get(row[y], ItemNode.class)) {
					list.add(new GroundItem(ctx, tile.derive(x, y), n));
				}
			}

		}
		return list;
	}

	@Override
	public GroundItem nil() {
		return new GroundItem(ctx, Tile.NIL, new ItemNode(null));
	}
}
