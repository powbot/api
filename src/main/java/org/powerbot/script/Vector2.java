package org.powerbot.script;

import java.awt.*;

/**
 * Vector2
 * An object representing a mathematical vector in 2D cartesian coordinate space.
 */
public class Vector2 implements Comparable<Vector2> {
	public final int x;
	public final int y;

	public Vector2() {
		this(0, 0);
	}

	public Vector2(final Point p) {
		this(p.x, p.y);
	}

	public Vector2(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2 add(final Vector2 u) {
		return new Vector2(x + u.x, y + u.y);
	}

	public Vector2 mul(final double u) {
		return new Vector2((int) (x * u), (int) (y * u));
	}

	public double cross(final Vector2 U, final double a) {
		return magnitude() * U.magnitude() * Math.sin(a);
	}

	public double dot(final Vector2 U) {
		return x * U.x + y * U.y;
	}

	public final double distanceTo(final Vector2 v) {
		return Math.sqrt(Math.pow(v.x - x, 2) + Math.pow(v.y - y, 2));
	}

	public final double gradientTo(final Vector2 v) {
		return (double) (v.y - y) / (v.x - x);
	}

	public final double angleTo(final Vector2 v) {
		double a = Math.atan2(v.y - y, v.x - x);

		if (a < 0) {
			a = Math.abs(a);
		} else {
			a = 2 * Math.PI - a;
		}

		return a;
	}

	public long toLong2D() {
		return (long) x << 32 | y & 0xffffffffL;
	}

	public int[] toMatrix() {
		return new int[]{x, y};
	}

	public Point toPoint() {
		return new Point(x, y);
	}

	public double magnitude() {
		return Math.sqrt(x * x + y * y);
	}

	@Override
	public int compareTo(final Vector2 o) {
		return y < o.y ? -1 : y > o.y ? 1 : Integer.compare(x, o.x);
	}

	@Override
	public String toString() {
		return String.format("(%s, %s)", x, y);
	}

	@Override
	public boolean equals(final Object o) {
		Vector2 v2;
		final Vector3 v3;
		return o instanceof Vector3 ? (v3 = (Vector3) o).z == 0 && v3.y == y && v3.x == x
				: o instanceof Vector2 && (v2 = (Vector2) o).y == y && v2.x == x;
	}

	@Override
	public int hashCode() {
		return (y & 0xffff) << 16 | (x & 0xffff);
	}
}
