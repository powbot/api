package org.powerbot.script;

import java.awt.*;

/**
 * Vector3
 * An object representing a mathematical vector in 3D cartesian coordinate space.
 */
public class Vector3 implements Comparable<Vector3> {
	public int x, y, z;

	public Vector3() {
		this(0, 0, 0);
	}

	public Vector3(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3 add(final Vector3 u) {
		return new Vector3(x + u.x, y + u.y, z + u.z);
	}

	public Vector3 mul(final double u) {
		return new Vector3((int) (x * u), (int) (y * u), (int) (z * u));
	}

	public double cross(final Vector3 u, final double a) {
		return magnitude() * u.magnitude() * Math.sin(a);
	}

	public double dot(final Vector3 u) {
		return x * u.x + y * u.y + z * u.z;
	}

	public double distanceTo2D(final Vector3 v) {
		return Math.sqrt(Math.pow(v.x - x, 2) + Math.pow(v.y - y, 2));
	}

	public double distanceTo(final Vector3 v) {
		return Math.sqrt(Math.pow(v.x - x, 2) + Math.pow(v.y - y, 2) + Math.pow(v.z - z, 2));
	}

	public final double gradientTo2D(final Vector3 v) {
		return (double) (v.y - y) / (v.x - x);
	}

	public final double angleTo2D(final Vector3 v) {
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
		return new int[]{x, y, z};
	}

	public Point toPoint2D() {
		return new Point(x, y);
	}

	public double magnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	@Override
	public int compareTo(final Vector3 o) {
		final long a, b;
		return z < o.z ? -1 : z > o.z ? 1 : (a = toLong2D()) < (b = o.toLong2D()) ? -1 : a > b ? 1 : 0;
	}

	@Override
	public String toString() {
		return String.format("(%s, %s, %s)", x, y, z);
	}

	@Override
	public boolean equals(final Object o) {
		final Vector2 v2;
		final Vector3 v3;
		return o instanceof Vector2 ? z == 0 && (v2 = (Vector2) o).y == y && v2.x == x
				: o instanceof Vector3 && (v3 = (Vector3) o).z == z && v3.y == y && v3.x == x;
	}

	@Override
	public int hashCode() {
		return ((z & 0x80) << 30) | ((y & 0x7fff) << 16) | (x & 0x7fff);
	}
}
