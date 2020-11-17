package org.powerbot.script.rt4;

import org.powbot.input.MouseMovement;
import org.powbot.input.MouseMovementCompleted;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.Condition;
import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;

import java.awt.*;
import java.util.concurrent.*;

/**
 * Movement
 */
public class Movement extends ClientAccessor {

	public Movement(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * Creates a new tile path.
	 *
	 * @param tiles The array of tiles in the path.
	 * @return the generated {@link TilePath}
	 */
	public TilePath newTilePath(final Tile... tiles) {
		if (tiles == null) {
			throw new IllegalArgumentException("tiles are null");
		}
		return new TilePath(ctx, tiles);
	}

	/**
	 * Creates a local path in the current region.
	 *
	 * @param locatable the destination tile
	 * @return the generated {@link LocalPath}
	 */
	public LocalPath findPath(final Locatable locatable) {
		if (locatable == null) {
			throw new IllegalArgumentException();
		}
		return new LocalPath(ctx, locatable);
	}

	/**
	 * Returns the destination of the player, as represented by the red flag on the mini-map while
	 * traversing. If the player is not moving, this will return {@link Tile#NIL}.
	 *
	 * @return The destination of the player.
	 */
	public Tile destination() {
		final Client client = ctx.client();
		if (client == null) {
			return Tile.NIL;
		}
		final int dX = client.getDestinationX(), dY = client.getDestinationY();
		if (dX <= 0 || dY <= 0) {
			return Tile.NIL;
		}
		return ctx.game.mapOffset().derive(dX, dY);
	}

	/**
	 * Attempts to use the mini-map to step towards the {@link Locatable}.
	 *
	 * @param locatable The location of where to step towards.
	 * @return {@code true} if successfully clicked the mini-map, {@code false} otherwise.
	 */
	public boolean step(final Locatable locatable) {
		Tile loc = locatable.tile();
		if (!new TileMatrix(ctx, loc).onMap()) {
			loc = closestOnMap(loc);
			if (!new TileMatrix(ctx, loc).onMap()) {
				return false;
			}
		}

		final TileMatrix tile = new TileMatrix(ctx, loc);
		final CompletableFuture<Boolean> interacted = new CompletableFuture<>();
		final Callable<Point> position = tile::mapPoint;
		final Callable<Boolean> valid = tile::onMap;

		final MouseMovementCompleted completed = (boolean result) ->
			interacted.complete(ctx.input.click(true));

		try {
			final MouseMovement movement = new MouseMovement(position, valid, completed, false);
			ctx.input.moveAsync(movement);
			return interacted.get(10, TimeUnit.SECONDS);
		} catch (InterruptedException | TimeoutException | ExecutionException e) {
			return false;
		}
	}

	/**
	 * Grabs the closest tile on map relative to the given {@link Locatable}.
	 *
	 * @param locatable The locatable
	 * @return the {@link Tile}
	 */
	public Tile closestOnMap(final Locatable locatable) {
		final Tile local = ctx.players.local().tile();
		final Tile tile = locatable.tile();
		if (local == Tile.NIL || tile == Tile.NIL) {
			return Tile.NIL;
		}
		if (new TileMatrix(ctx, tile).onMap()) {
			return tile;
		}
		final int x2 = local.x();
		final int y2 = local.y();
		int x1 = tile.x();
		int y1 = tile.y();
		final int dx = Math.abs(x2 - x1);
		final int dy = Math.abs(y2 - y1);
		final int sx = (x1 < x2) ? 1 : -1;
		final int sy = (y1 < y2) ? 1 : -1;
		int off = dx - dy;
		for (; ; ) {
			final Tile t = new Tile(x1, y1, local.floor());
			if (new TileMatrix(ctx, t).onMap()) {
				return t;
			}
			if (x1 == x2 && y1 == y2) {
				break;
			}
			final int e2 = 2 * off;
			if (e2 > -dy) {
				off = off - dy;
				x1 = x1 + sx;
			}
			if (e2 < dx) {
				off = off + dx;
				y1 = y1 + sy;
			}
		}
		return Tile.NIL;
	}

	/**
	 * The run energy of the player.
	 *
	 * @return The energy of the player, which is 0-100.
	 */
	public int energyLevel() {
		final Client c = ctx.client();
		return c != null ? c.getRunPercentage() : -1;
	}

	/**
	 * Whether or not the player is running.
	 *
	 * @return {@code true} if the player is running, {@code false} otherwise.
	 */
	public boolean running() {
		return ctx.varpbits.varpbit(Constants.MOVEMENT_RUNNING) == 0x1;
	}

	/**
	 * Attempts to set the player to the specified running state.
	 *
	 * @param running {@code true} to run, {@code false} to walk.
	 * @return {@code true} if the state was successfully set, {@code false} otherwise.
	 */
	public boolean running(final boolean running) {
		return running == running() || (ctx.widgets.widget(Constants.MOVEMENT_MAP).component(Constants.MOVEMENT_RUN_ENERGY - 1).interact("Toggle Run") &&
			Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					return running() == running;
				}
			}, 20, 10));
	}

	/**
	 * Returns the amount of steps between two locations. This will return -1 if
	 * the amount of steps was indeterminate (for example, one of the locations wasn't loaded or
	 * they are both not on the same floor).
	 * <br><br>
	 * Note: this can be resource intensive, as it generates a path between these
	 * two locations to find the distance. This should only be used if you need an accurate
	 * measurement for the amount of steps required. Please consider using
	 * {@link Tile#distanceTo(Locatable)} for euclidean distance for the length of a straight
	 * line between these two points.
	 *
	 * @param l1 Location A
	 * @param l2 Location B
	 * @return The amount of steps required to traverse between these two locations.
	 */
	public int distance(final Locatable l1, final Locatable l2) {
		final Tile b = ctx.game.mapOffset();
		Tile t1, t2;
		if (b == null ||
			l1 == null || (t1 = l1.tile()) == null ||
			l2 == null || (t2 = l2.tile()) == null ||
			b == Tile.NIL || t1 == Tile.NIL || t2 == Tile.NIL
			|| l1.tile().floor() != l2.tile().floor()
			|| l1.tile().floor() != b.floor()) {
			return -1;
		}
		t1 = t1.derive(-b.x(), -b.y());
		t2 = t2.derive(-b.x(), -b.y());

		final LocalPath.Graph graph = LocalPath.getGraph(ctx);
		final LocalPath.Node[] path;
		final LocalPath.Node nodeStart, nodeStop;
		if (graph != null &&
			(nodeStart = graph.getNode(t1.x(), t1.y())) != null &&
			(nodeStop = graph.getNode(t2.x(), t2.y())) != null) {
			LocalPath.bfs(graph, nodeStart, nodeStop);
			path = LocalPath.follow(nodeStop);
		} else {
			path = new LocalPath.Node[0];
		}
		final int l = path.length;
		return l > 0 ? l : -1;
	}

	/**
	 * Returns the amount of steps between the player and the location. This will return -1 if
	 * the amount of steps was indeterminate (for example, one of the locations wasn't loaded or
	 * they are both not on the same floor).
	 * <br><br>
	 * Note: this can be resource intensive, as it generates a path between these
	 * two locations to find the distance. This should only be used if you need an accurate
	 * measurement for the amount of steps required. Please consider using
	 * {@link Tile#distanceTo(Locatable)} for euclidean distance for the length of a straight
	 * line between these two points.
	 *
	 * @param l The destination
	 * @return The amount of steps required to traverse between the player and the location.
	 */
	public int distance(final Locatable l) {
		return distance(ctx.players.local(), l);
	}

	/**
	 * Whether or not the location may be reached. This will return false if one of the locations
	 * is not loaded, they're not on the same plane, or the distance between them is 0.
	 * <br><br>
	 * Note: this can be resource intensive, as it generates a path between these
	 * two locations to determine if they're reachable. This should only be used if you need
	 * to be certain that the point is reachable. Please consider using
	 * {@link Tile#distanceTo(Locatable)}
	 *
	 * @param l1 Point A
	 * @param l2 Point B
	 * @return {@code true} if the two points can reach each other, {@code false} otherwise.
	 */
	public boolean reachable(final Locatable l1, final Locatable l2) {
		return distance(l1, l2) > 0;
	}
}
