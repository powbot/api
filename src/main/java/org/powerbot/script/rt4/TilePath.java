package org.powerbot.script.rt4;

import org.powerbot.script.*;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TilePath
 */
public class TilePath extends Path {
	private final AtomicInteger run_energy, spaced_action;
	protected Tile[] tiles;
	protected Tile[] orig;
	private boolean end;
	private Tile last;

	public TilePath(final ClientContext ctx, final Tile[] tiles) {
		super(ctx);
		run_energy = new AtomicInteger(-1);
		spaced_action = new AtomicInteger(-1);
		orig = tiles;
		this.tiles = Arrays.copyOf(tiles, tiles.length);
	}

	@Override
	public boolean traverse(final EnumSet<TraversalOption> options) {
		final Player local = ctx.players.local();
		final Tile next = next();
		if (next == null || local == null) {
			return false;
		}
		final Tile dest = ctx.movement.destination();
		if (next.equals(end())) {
			if (next.distanceTo(ctx.players.local()) <= 2) {
				return false;
			}
			if (end && (local.inMotion() || dest.equals(next))) {
				return false;
			}
			end = true;
		} else {
			end = false;
		}
		if (options != null) {
			if (options.contains(TraversalOption.HANDLE_RUN) && !ctx.movement.running()) {
				final int e = ctx.movement.energyLevel();
				run_energy.compareAndSet(-1, Random.nextInt(20, 90));
				if (e >= run_energy.get() && ctx.movement.running(true)) {
					run_energy.set(-1);
				}
			}

			if (options.contains(TraversalOption.SPACE_ACTIONS) &&
					local.inMotion() && dest.distanceTo(last) > 3d) {
				spaced_action.compareAndSet(-1, Random.nextInt(5, 12));
				final double d, d2 = dest.distanceTo(local);
				if (d2 > spaced_action.get()) {
					d = d2;
				} else {
					final double d3 = ctx.movement.distance(dest);
					d = d3 != -1 ? d3 : d2;
				}
				if (d > (double) spaced_action.get()) {
					return true;
				}
			}
		}
		last = next;
		if (ctx.movement.step(next)) {
			spaced_action.set(-1);
			if (local.inMotion()) {
				return Condition.wait(new Condition.Check() {
					@Override
					public boolean poll() {
						return ctx.movement.destination().distanceTo(next) < 3;
					}
				}, 60, 10);
			}
			return next.distanceTo(ctx.players.local()) < 5d || Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					return ctx.players.local().inMotion() && ctx.movement.destination().distanceTo(next) < 3;
				}
			}, 125, 10);
		}
		return false;
	}

	@Override
	public boolean valid() {
		return tiles.length > 0 && next() != null && end().distanceTo(ctx.players.local()) > Math.sqrt(2);
	}

	@Override
	public Tile next() {
		/* Wait for map not to be loading */
		final int state = ctx.game.clientState();
		if (state == Constants.GAME_LOADING) {
			Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					return ctx.game.clientState() != Constants.GAME_LOADING;
				}
			});
			return next();
		}
		if (state != Constants.GAME_LOADED) {
			return null;
		}
		/* Get current destination */
		final Tile dest = ctx.movement.destination();
		/* Label main loop for continuing purposes */
		out:
		/* Iterate over all tiles but the first tile (0) starting with the last (length - 1). */
		for (int i = tiles.length - 1; i > 0; --i) {
			/* The tiles not in view, go to the next. */
			if (!tiles[i].matrix(ctx).valid() || !tiles[i].matrix(ctx).onMap()) {
				continue;
			}
			/* If our destination is NIL, assume mid path and continue there. */
			/* LARGELY SPACED PATH SUPPORT: If the current destination is the tile on the map, return that tile
			 * as the next one will be coming soon (we hope/assume this, as short spaced paths should never experience
			 * this condition as one will be on map before it reaches the current target). */
			if (dest == Tile.NIL) {
				if (tiles[i].matrix(ctx).reachable()) {
					return tiles[i];
				}
				continue;
			} else if (tiles[i].distanceTo(dest) < 3d) {
				return tiles[i];
			}
			/* Tile is on map and isn't currently "targeted" (dest), let's check it out.
			 * Iterate over all tiles succeeding it. */
			for (int a = i - 1; a >= 0; --a) {
				/* The tile before the tile on map isn't on map.  Break out to the next tile.
				 * Explanation: Path wraps around something and must be followed.
				 * We cannot suddenly click out of a "pathable" region (104x104).
				 * In these cases, we can assume a better tile will become available. */
				if (!tiles[a].matrix(ctx).valid() || !tiles[a].matrix(ctx).onMap()) {
					continue out;
				}
				/* If a tile (successor) is currently targeted, return the tile that was the "best"
				 * on the map for getNext as we can safely assume we're following our path. */
				if (tiles[a].distanceTo(dest) < 3d) {
					return tiles[i];
				}
			}
		}
		/* Well, we've made it this far.  Return the first tile if nothing else is on our map.
		* CLICKING BACK AND FORTH PREVENTION: check for dest not to be null if we're just starting
		 * our path.  If our destination isn't null and we somehow got to our first tile then
		 * we can safely assume lag is being experienced and return null until next call of getNext.
		 * TELEPORTATION SUPPORT: If destination is set but but we're not moving, assume
		 * invalid destination tile from teleportation reset and return first tile. */
		final Player p = ctx.players.local();
		if (p != null && !p.inMotion() && dest != Tile.NIL) {
			for (int i = tiles.length - 1; i >= 0; --i) {
				if (tiles[i].matrix(ctx).onMap()) {
					return tiles[i];
				}
			}
		}
		if (tiles.length == 0 || !tiles[0].matrix(ctx).onMap()) {
			return null;
		}
		return tiles[0];
	}

	@Override
	public Tile start() {
		return tiles[0];
	}

	@Override
	public Tile end() {
		return tiles[tiles.length - 1];
	}

	public TilePath randomize(final int maxX, final int maxY) {
		for (int i = 0; i < tiles.length; ++i) {
			tiles[i] = orig[i].derive(Random.nextInt(-maxX, maxX + 1), Random.nextInt(-maxY, maxY + 1));
		}
		return this;
	}

	public TilePath reverse() {
		Tile[] reversed = new Tile[tiles.length];
		for (int i = 0; i < orig.length; ++i) {
			reversed[i] = orig[tiles.length - 1 - i];
		}
		orig = reversed;
		reversed = new Tile[tiles.length];
		for (int i = 0; i < tiles.length; ++i) {
			reversed[i] = tiles[tiles.length - 1 - i];
		}
		tiles = reversed;
		return this;
	}

	public Tile[] toArray() {
		final Tile[] a = new Tile[tiles.length];
		System.arraycopy(tiles, 0, a, 0, tiles.length);
		return a;
	}
}
