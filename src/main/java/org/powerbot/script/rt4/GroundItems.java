package org.powerbot.script.rt4;

import org.jetbrains.annotations.NotNull;
import org.powbot.stream.locatable.interactive.GroundItemStream;
import org.powbot.stream.Streamable;
import org.powerbot.bot.rt4.*;
import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.extended.IMobileClient;
import org.powerbot.bot.rt4.client.internal.IItemNode;
import org.powerbot.bot.rt4.client.internal.INodeDeque;
import org.powerbot.script.Tile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * GroundItems
 */
public class GroundItems extends BasicQuery<GroundItem> implements Streamable<GroundItemStream> {
	public GroundItems(final ClientContext ctx) {
		super(ctx);
	}

	public BasicQuery<GroundItem> select(final int radius) {
		final IClient client = ctx.client();
		return select(get(radius, client != null ? client.getFloor() : -1));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GroundItem> get() {
		final IClient client = ctx.client();
		return get(104, client != null ? client.getFloor() : -1);
	}

	private List<GroundItem> get(int radius, final int floor) {
		if (ctx.client().isMobile()) {
			return getMobileGroundItems(radius, floor);
		} else {
			return getDesktopGroundItems(radius, floor);
		}
	}

	private List<GroundItem> getMobileGroundItems(int radius, int floor) {
		final Tile currentPosition = ctx.players.local().tile();
		return ((IMobileClient) ctx.client()).getAllGroundItems().stream()
			.filter(it -> currentPosition.distanceTo(it) < radius && it.tile().floor() == floor)
			.collect(Collectors.toList());
	}

	@NotNull
	private List<GroundItem> getDesktopGroundItems(int radius, int floor) {
		if (radius < 1) {
			radius = 110;
		}
		final List<GroundItem> r = new CopyOnWriteArrayList<>();
		final IClient client = ctx.client();
		final INodeDeque[][][] dequeArray;
		if (client == null || (dequeArray = client.getGroundItems()) == null) {
			return r;
		}
		final INodeDeque[][] rows;
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
			final INodeDeque[] row = rows[x];
			if (row == null) {
				continue;
			}
			for (int y = Math.max(0, ct.y() - radius); y < Math.min(row.length, ct.y() + radius + 1); y++) {
				for (final IItemNode n : NodeQueue.get(row[y], IItemNode.class)) {
					list.add(new GroundItem(ctx, tile.derive(x, y), n));
				}
			}

		}
		return list;
	}

	@Override
	public GroundItem nil() {
		return GroundItem.NIL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroundItemStream toStream() {
		return new GroundItemStream(ctx, get().stream());
	}
}
