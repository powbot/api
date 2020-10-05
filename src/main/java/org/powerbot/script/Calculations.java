package org.powerbot.script;

import java.awt.*;

/**
 * Calculations
 * A utility class for converting cartesian coordinates from 3D to 2D.
 */
public class Calculations {
	/**
	 * Generates a normative point based on bounded data and juxtaposition.
	 * Maintains bias on object and ensures uniform distribution; leaves
	 * normality to boundaries.
	 *
	 * @param b Bounding rectangle.
	 * @param o Object rectangle.
	 * @return a normative point
	 */
	public static Point nextPoint(final Rectangle b, final Rectangle o) {
		//boundary data
		final double[] d = {b.x, b.y, b.width, b.height, o.width, o.height};
		if (Math.min(b.width, Math.min(b.height, Math.min(o.width, o.height))) < 1) {
			return new Point(-1, -1);
		}

		//uniform ellipse distribution
		final double rho = Math.sqrt(Random.nextDouble());
		final double phi = 2d * Math.PI * Random.nextDouble();

		//gaussian ellipse scaling
		final double w = d[4] + (Random.nextGaussian() * (d[2] - d[4]) / 1.5);
		final double h = d[5] + (Random.nextGaussian() * (d[3] - d[5]) / 1.5);

		//ellipse point calculation
		final double zx = rho * Math.cos(phi) * (w / 2d), zy = rho * Math.sin(phi) * (h / 2d);

		//relative to object boundaries
		final double x = o.x + d[4] / 2d + zx;
		final double y = o.y + d[5] / 2d + zy;

		//boundary check
		if (x < d[0] || y < d[1] || x >= d[0] + d[2] || y >= d[1] + d[3]) {
			final double rx = d[0] + Random.nextDouble(0d, d[2]), ry = d[1] + Random.nextDouble(0d, d[3]);
			return new Point((int) rx, (int) ry);
		}
		return new Point((int) x, (int) y);
	}
}
