package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.*;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.bot.rt4.client.*;
import org.powerbot.script.Tile;
import org.powerbot.script.*;

import java.awt.*;

/**
 * GroundItem
 */
public class GroundItem extends GenericItem implements Nameable, InteractiveEntity, Identifiable, Validatable, Actionable {
	public static final Color TARGET_COLOR = new Color(255, 255, 0, 75);
	private final TileMatrix tile;
	private final ItemNode node;

	/**
	 * A representation of an item which is currently on the floor of the game.
	 *
	 * @param ctx The ClientContext
	 * @param tile The tile the item is located on
	 * @param node The ItemNode
	 */
	GroundItem(final ClientContext ctx, final Tile tile, final ItemNode node) {
		super(ctx);//TODO: valid
		this.tile = tile.matrix(ctx);
		boundingModel = this.tile.boundingModel;
		this.node = node;
		bounds(-8, 8, -8, 0, -8, 8);
	}

	@Override
	public void bounds(final int x1, final int x2, final int y1, final int y2, final int z1, final int z2) {
		tile.bounds(x1, x2, y1, y2, z1, z2);
	}

	@Override
	public int id() {
		return node.getItemId();
	}

	/**
	 * The stack size of the ItemNode.
	 *
	 * @return Stack size
	 */
	public int stackSize() {
		return node.getStackSize();
	}

	@Override
	public Point centerPoint() {
		return tile.centerPoint();
	}

	@Override
	public Point nextPoint() {
		return tile.nextPoint();
	}

	@Override
	public boolean contains(final Point point) {
		return tile.contains(point);
	}

	@Override
	public Tile tile() {
		return tile.tile();
	}

	@Override
	public int hashCode() {
		return node.hashCode();
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof GroundItem && tile.equals(((GroundItem) o).tile) && node.equals(((GroundItem) o).node);
	}

	@Override
	public String toString() {
		return String.format("%s[id=%d,stack=%d,tile=%s]", GroundItem.class.getName(), id(), stackSize(), tile.tile().toString());
	}

	@Override
	public String[] actions() {
		return groundActions();
	}

	@Override
	public boolean valid() {
		final Client c = ctx.client();
		if (c == null || node.isNull()) {
			return false;
		}
		final NodeDeque[][][] nd = c.getGroundItems();
		if (nd != null) {
			final int f = c.getFloor();
			if (f < 0 || f >= nd.length || nd[f] == null) {
				return false;
			}
			final Tile t = tile.tile().derive(-c.getOffsetX(), -c.getOffsetY());
			if (t.x() < 0 || t.y() < 0 || t.x() >= nd[f].length) {
				return false;
			}
			final NodeDeque[] nd2 = nd[f][t.x()];
			if (nd2 == null || t.y() >= nd2.length) {
				return false;
			}
			final NodeDeque d = nd2[t.y()];
			return d != null && NodeQueue.get(d, ItemNode.class).contains(node);
		}
		return false;
	}
}
