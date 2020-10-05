package org.powerbot.script;

import org.powerbot.script.rt4.TileMatrix;

import java.awt.*;

/**
 * Tile
 * An object representing a 2D point within the Runescape world map.
 */
public class Tile implements Locatable, Nillable<Tile>, Comparable<Tile> {
	public static final Tile NIL = new Tile(-1, -1, -1);
	public static final Color TARGET_COLOR = new Color(255, 0, 0, 75);
	protected final Vector3 p;

	public Tile(final int x, final int y) {
		this(x, y, 0);
	}

	public Tile(final int x, final int y, final int z) {
		p = new Vector3(x, y, z);
	}

	/**
	 * Converts an integer matrix into a Tile array.
	 * 
	 * @param list The integer matrix to convert from.
	 * @return The converted Tile array.
	 */
	public static Tile[] fromArray(final int[][] list) {
		final Tile[] t = new Tile[list.length];
		for (int i = 0; i < t.length; i++) {
			final int l = list[i].length;
			t[i] = new Tile(l > 0 ? list[i][0] : 0, l > 1 ? list[i][1] : 0, list[i].length > 2 ? list[i][2] : 0);
		}
		return t;
	}

	/**
	 * The point which represents where the tile is on the x-axis.
	 * 
	 * @return Where the tile lies on the x-axis.
	 */
	public int x() {
		return p.x;
	}

	/**
	 * The point which represents where the tile is on the y-axis.
	 * 
	 * @return Where the tile lies on the y-axis.
	 */
	public int y() {
		return p.y;
	}


	/**
	 * The point which represents where the tile is on the z-axis.
	 * 
	 * @return Where the tile lies on the z-axis.
	 */
	public int floor() {
		return p.z;
	}

	/**
	 * Calculates the euclidean distance between the Tile and the given
	 * {@link Locatable}.
	 * 
	 * @param l The secondary point to calculate the distance from.
	 * @return the distance between the two points. If either points are
	 * {@code null}, or they are on separate floors, it will return
	 * {@link Double#POSITIVE_INFINITY}.
	 */
	public double distanceTo(final Locatable l) {
		final Tile o;
		return l == null || (o = l.tile()) == null || p.z != o.p.z || o.p.z == NIL.p.z ? Double.POSITIVE_INFINITY : p.distanceTo(o.p);
	}

	/**
	 * Calculates a derivative with the specified parameters.
	 * 
	 * @param x The relative amount to shift on the x-axis.
	 * @param y The relative amount to shift on the y-axis.
	 * @return The derived tile from the specified values.
	 */
	public Tile derive(final int x, final int y) {
		return new Tile(p.x + x, p.y + y, p.z);
	}

	/**
	 * Calculates a derivative with the specified parameters.
	 * 
	 * @param x The relative amount to shift on the x-axis.
	 * @param y The relative amount to shift on the y-axis.
	 * @param z The relative amount to shift on the z-axis.
	 * @return The derived tile from the specified values.
	 */
	public Tile derive(final int x, final int y, final int z) {
		return new Tile(p.x + x, p.y + y, z);
	}

	/**
	 * Constructs a {@link TileMatrix} for the given Tile.
	 * 
	 * @param ctx The {@link ClientContext}.
	 * @return The constructed TileMatrix.
	 */
	public TileMatrix matrix(final org.powerbot.script.rt4.ClientContext ctx) {
		return new TileMatrix(ctx, this);
	}

	@Override
	public Tile tile() {
		return this;
	}

	@Override
	public Tile nil() {
		return NIL;
	}

	@Override
	public int compareTo(final Tile o) {
		return p.compareTo(o.p);
	}

	@Override
	public String toString() {
		return p.toString();
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof Tile && p.equals(((Tile) o).p);
	}

	@Override
	public int hashCode() {
		return p.hashCode();
	}
}
