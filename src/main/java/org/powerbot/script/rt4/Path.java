package org.powerbot.script.rt4;

import org.powerbot.script.*;

import java.util.EnumSet;

/**
 * Path
 */
public abstract class Path extends ClientAccessor implements Validatable {
	public static final int WALL_NORTHWEST = 0x1;
	public static final int WALL_NORTH = 0x2;
	public static final int WALL_NORTHEAST = 0x4;
	public static final int WALL_EAST = 0x8;
	public static final int WALL_SOUTHEAST = 0x10;
	public static final int WALL_SOUTH = 0x20;
	public static final int WALL_SOUTHWEST = 0x40;
	public static final int WALL_WEST = 0x80;
	public static final int OBJECT_TILE = 0x100;
	public static final int DECORATION_BLOCK = 0x40000;
	public static final int OBJECT_BLOCK = 0x200000;
	public static final int BLOCKED = OBJECT_TILE | OBJECT_BLOCK | DECORATION_BLOCK;

	Path(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * Takes a step along this path if appropriate.
	 * If the path cannot be traversed due to the player being too far from its vertices or already at the end vertex,
	 * {@code false} will be returned. In all other cases, {@code true} will be returned,
	 * but an action will not necessarily be performed (based on the given options).
	 *
	 * @param options Walking style options.
	 * @return {@code true} if this path is currently valid for the player; otherwise {@code false}.
	 */
	public abstract boolean traverse(EnumSet<TraversalOption> options);

	/**
	 * Takes a step along this path if appropriate.
	 * Specifies only TraversalOption.SPACE_ACTIONS.
	 *
	 * @return {@code true} if this path is currently valid for the player; otherwise {@code false}.
	 * @see #traverse(java.util.EnumSet)
	 */
	public boolean traverse() {
		return traverse(EnumSet.of(TraversalOption.HANDLE_RUN, TraversalOption.SPACE_ACTIONS));
	}

	/**
	 * Gets the next immediately available vertex in this path.
	 *
	 * @return The next walkable <code>Tile</code>.
	 */
	public abstract Tile next();

	/**
	 * Gets the start tile of this path.
	 *
	 * @return The start <code>Tile</code>.
	 */
	public abstract Tile start();

	/**
	 * Gets the end tile of this path.
	 *
	 * @return The end <code>Tile</code>.
	 */
	public abstract Tile end();

	/**
	 * Defines the path traversal options.
	 */
	public enum TraversalOption {
		HANDLE_RUN, SPACE_ACTIONS
	}
}
