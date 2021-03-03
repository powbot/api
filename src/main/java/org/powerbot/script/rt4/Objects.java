package org.powerbot.script.rt4;

import org.powbot.stream.locatable.interactive.GameObjectStream;
import org.powbot.stream.Streamable;
import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.internal.IBasicObject;
import org.powerbot.bot.rt4.client.internal.IGameObject;
import org.powerbot.bot.rt4.client.internal.ITile;
import org.powerbot.script.Locatable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Objects
 */
public class Objects extends BasicQuery<GameObject> implements Streamable<GameObjectStream> {

	public Objects(final ClientContext ctx) {
		super(ctx);
	}

	public BasicQuery<GameObject> select(final int radius) {
		return select(get(radius));
	}

	public BasicQuery<GameObject> select(final Locatable l, final int radius) {
		return select(get(l.tile(), radius));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GameObject> get() {
		return get(Integer.MAX_VALUE);
	}

	public List<GameObject> get(final int radius) {
		return get(ctx.players.local().tile(), radius);
	}

	public List<GameObject> get(final Locatable l, int radius) {
		radius = Math.min(radius, 110);
		final List<GameObject> r = new ArrayList<>();
		final IClient client = ctx.client();
		if (client == null) {
			return r;
		}
		final ITile[][][] tiles = client.getLandscape().getTiles();
		final int floor = client.getFloor();
		if (floor < 0 || floor >= tiles.length) {
			return r;
		}
		final ITile[][] rows = tiles[floor];
		final HashSet<GameObject> set = new HashSet<>();
		int start_x = 0, end_x = Integer.MAX_VALUE, start_y = 0, end_y = Integer.MAX_VALUE;
		if (radius >= 0) {
			final org.powerbot.script.Tile mo = ctx.game.mapOffset(), lp = l.tile();
			if (mo != org.powerbot.script.Tile.NIL && lp != org.powerbot.script.Tile.NIL) {
				final org.powerbot.script.Tile t = lp.derive(-mo.x(), -mo.y());
				start_x = t.x() - radius;
				end_x = t.x() + radius;
				start_y = t.y() - radius;
				end_y = t.y() + radius;
			}
		}
		for (int x = Math.max(0, start_x); x <= Math.min(end_x, rows.length - 1); x++) {
			final ITile[] col = rows[x];
			for (int y = Math.max(0, start_y); y <= Math.min(end_y, col.length - 1); y++) {
				final ITile tile = col[y];
				if (tile == null) {
					continue;
				}

				for (final IBasicObject obj :
					new IBasicObject[]{tile.getBoundaryObject(), tile.getFloorObject(), tile.getWallObject()}) {
					if (obj != null) {
						set.add(new GameObject(ctx, new BasicObject<>(obj), getType(obj)));
					}
				}
				for (final IGameObject gameObject : tile.getGameObjects()) {
					if (gameObject != null) {
						set.add(new GameObject(ctx, new BasicObject<>(gameObject), getType(gameObject)));
					}
				}
			}
		}
		return new ArrayList<>(set);
	}

	private GameObject.Type getType(IBasicObject o) {
		final int t = o.getMeta() & 0x3f;
		if (t == 0 || t == 1 || t == 9) {
			return GameObject.Type.BOUNDARY;
		} else if (t == 2 || t == 3 || t == 4 || t == 5 || t == 6 || t == 7 || t == 8) {
			return GameObject.Type.WALL_DECORATION;
		} else if (t == 10 || t == 11) {
			return GameObject.Type.INTERACTIVE;
		} else if (t == 22) {
			return GameObject.Type.FLOOR_DECORATION;
		}

		return GameObject.Type.UNKNOWN;
	}

	@Override
	public GameObject nil() {
		return GameObject.NIL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GameObjectStream toStream() {
		return new GameObjectStream(ctx, get().stream());
	}


}
