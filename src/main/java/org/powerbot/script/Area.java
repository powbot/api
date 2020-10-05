package org.powerbot.script;

import java.awt.*;
import java.util.Arrays;

/**
 * Area
 * A two-dimensional, closed area within the Runescape world map.
 */
public class Area {
	private final Polygon polygon;
	private final int plane;
	private Tile[] tiles;

	public Area(final Tile t1, final Tile t2) {
		this(
				new Tile(Math.min(t1.x(), t2.x()), Math.min(t1.y(), t2.y()), t1.floor()),
				new Tile(Math.max(t1.x(), t2.x()), Math.min(t1.y(), t2.y()), t1.floor()),
				new Tile(Math.max(t1.x(), t2.x()), Math.max(t1.y(), t2.y()), t2.floor()),
				new Tile(Math.min(t1.x(), t2.x()), Math.max(t1.y(), t2.y()), t2.floor())
		);
	}

	public Area(final Tile... tiles) {
		if (tiles.length == 0) {
			throw new IllegalArgumentException("tiles.length < 0");
		}
		this.polygon = new Polygon();
		this.plane = tiles[0].floor();
		for (final Tile tile : tiles) {
			if (tile.floor() != this.plane) {
				throw new IllegalArgumentException("mismatched planes " + plane + " != " + tile.floor());
			}
			polygon.addPoint(tile.x(), tile.y());
		}
		this.tiles = null;
	}

	public boolean contains(final Locatable... locatables) {
		for (final Locatable locatable : locatables) {
			final Tile tile = locatable.tile();
			if (tile.floor() != plane || !polygon.contains(tile.x(), tile.y())) {
				return false;
			}
		}
		return true;
	}

	public boolean containsOrIntersects(final Locatable... locatables) {
		for (final Locatable locatable : locatables) {
			final Tile tile = locatable.tile();
			if (tile.floor() != plane || (!polygon.contains(tile.x(), tile.y()) && !polygon.intersects(tile.x() - 0.5, tile.y() - 0.5, 1, 1))) {
				return false;
			}
		}
		return true;
	}

	public Tile getCentralTile() {
		final Point point = PolygonUtils.getCenter(polygon);
		return new Tile(point.x, point.y, plane);
	}

	public Tile getRandomTile() {
		final Tile[] tiles = getTiles();
		final int len = tiles.length;
		return len != 0 ? tiles[Random.nextInt(0, len)] : Tile.NIL;
	}

	public Tile getClosestTo(final Locatable locatable) {
		final Tile t = locatable != null ? locatable.tile() : Tile.NIL;
		if (t != Tile.NIL) {
			double dist = Double.POSITIVE_INFINITY;
			Tile tile = Tile.NIL;
			final Tile[] tiles = getTiles();
			for (final Tile item : tiles) {
				final double d = t.distanceTo(item);
				if (d < dist) {
					dist = d;
					tile = item;
				}
			}
			return tile;
		}
		return Tile.NIL;
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public Tile[] tiles() {
		return getTiles().clone();
	}

	private Tile[] getTiles() {
		if (this.tiles != null) {
			return this.tiles;
		}

		final Rectangle r = polygon.getBounds();
		int c = 0;
		final Tile[] tiles = new Tile[r.width * r.height];
		for (int x = 0; x < r.width; x++) {
			for (int y = 0; y < r.height; y++) {
				final int _x = r.x + x;
				final int _y = r.y + y;
				if (polygon.contains(_x, _y)) {
					tiles[c++] = new Tile(_x, _y, plane);
				}
			}
		}
		return this.tiles = Arrays.copyOf(tiles, c);
	}

	private double avg(final int... nums) {
		long total = 0;
		for (final int i : nums) {
			total += i;
		}
		return (double) total / (double) nums.length;
	}

	/**
	 * PolygonUtils
	 * http://www.shodor.org/~jmorrell/interactivate/org/shodor/util11/PolygonUtils.java
	 */
	private static class PolygonUtils {
		/**
		 * Finds the centroid of a polygon with integer verticies.
		 *
		 * @param pg The polygon to find the centroid of.
		 * @return The centroid of the polygon.
		 */

		public static Point getCenter(final Polygon pg) {
			if (pg == null) {
				return null;
			}

			final int N = pg.npoints;
			final Point[] polygon = new Point[N];

			for (int q = 0; q < N; q++) {
				polygon[q] = new Point(pg.xpoints[q], pg.ypoints[q]);
			}

			double cx = 0, cy = 0;
			final double A = getArea(polygon, N);
			int i, j;

			double factor;
			for (i = 0; i < N; i++) {
				j = (i + 1) % N;
				factor = (polygon[i].x * polygon[j].y - polygon[j].x * polygon[i].y);
				cx += (polygon[i].x + polygon[j].x) * factor;
				cy += (polygon[i].y + polygon[j].y) * factor;
			}
			factor = 1.0 / (6.0 * A);
			cx *= factor;
			cy *= factor;
			return new Point((int) Math.abs(Math.round(cx)), (int) Math.abs(Math.round(cy)));
		}

		/**
		 * Computes the area of any two-dimensional polygon.
		 *
		 * @param polygon The polygon to compute the area of input as an array of points
		 * @param N       The number of points the polygon has, first and last point
		 *                inclusive.
		 * @return The area of the polygon.
		 */
		public static double getArea(final Point[] polygon, final int N) {
			int i, j;
			double area = 0;

			for (i = 0; i < N; i++) {
				j = (i + 1) % N;
				area += polygon[i].x * polygon[j].y;
				area -= polygon[i].y * polygon[j].x;
			}

			area /= 2.0;
			return (Math.abs(area));
		}
	}
}
