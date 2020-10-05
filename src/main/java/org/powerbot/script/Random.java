package org.powerbot.script;

import java.security.SecureRandom;

/**
 * Random
 * A thread safe secure random number generating utiity.
 */
public class Random {
	private static final double[] pd;

	private static final ThreadLocal<java.util.Random> random = ThreadLocal.withInitial(() -> {
		java.util.Random r;
		try {
			r = SecureRandom.getInstance("SHA1PRNG", "SUN");
		} catch (final Exception ignored) {
			r = new java.util.Random();
		}
		r.setSeed(r.nextLong());
		return r;
	});

	static {
		pd = new double[2];
		final double[] e = {3d, 45d + random.get().nextInt(11), 12d + random.get().nextGaussian()};
		final double[] x = {Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().maxMemory() >> 30};
		pd[0] = 4d * Math.log(Math.sin(((Math.PI / x[0]) * Math.PI + 1d) / 4d)) / Math.PI + 2d * Math.PI * (Math.PI / x[0]) / 3d - 4d * Math.log(Math.sin(0.25d)) / Math.PI;
		pd[0] = e[0] * Math.exp(Math.pow(pd[0], 0.75d)) + e[1];
		pd[1] = e[2] * Math.exp(1d / Math.cosh(x[1]));
	}

	/**
	 * Returns a suggested human reaction delay.
	 *
	 * @return a random number
	 */
	public static int getDelay() {
		return (int) ((-1 + 2 * nextDouble()) * pd[1] + pd[0]);
	}

	/**
	 * Returns the hicks value.
	 *
	 * @param a the depth argument
	 * @return the computed hicks value
	 */
	public static int hicks(final int a) {
		return 105 * (int) (Math.log(a * 2) / 0.6931471805599453d /* log 2 */);
	}

	/**
	 * Generates a random boolean.
	 *
	 * @return returns true or false randomly
	 */
	public static boolean nextBoolean() {
		return random.get().nextBoolean();
	}

	/**
	 * Returns a pseudo-generated random number.
	 *
	 * @param min minimum bound (inclusive)
	 * @param max maximum bound (exclusive)
	 * @return the random number between min and max (inclusive, exclusive)
	 */
	public static int nextInt(final int min, final int max) {
		final int a = Math.min(min, max), b = Math.max(max, min);
		return a + (b == a ? 0 : random.get().nextInt(b - a));
	}

	/**
	 * Returns the next pseudo-random double, distributed between min and max.
	 *
	 * @param min the minimum bound
	 * @param max the maximum bound
	 * @return the random number between min and max
	 */
	public static double nextDouble(final double min, final double max) {
		final double a = Math.min(min, max), b = Math.max(max, min);
		return a + random.get().nextDouble() * (b - a);
	}

	/**
	 * Returns the next pseudo-random double.
	 *
	 * @return the next pseudo-random, a value between {@code 0.0} and {@code 1.0}.
	 */
	public static double nextDouble() {
		return random.get().nextDouble();
	}

	/**
	 * Returns the next pseudorandom, Gaussian ("normally") distributed {@code double} value with mean {@code 0.0} and
	 * standard deviation {@code 1.0}.
	 *
	 * @return a gaussian distributed number
	 */
	public static double nextGaussian() {
		return random.get().nextGaussian();
	}

	/**
	 * Returns a pseudo-random gaussian distributed number between the given min and max with the provided standard deviation.
	 *
	 * @param min the minimum bound
	 * @param max the maximum bound
	 * @param sd  the standard deviation from the mean
	 * @return a gaussian distributed number between the provided bounds
	 */
	public static int nextGaussian(final int min, final int max, final double sd) {
		return nextGaussian(min, max, (max - min) / 2, sd);
	}

	/**
	 * Returns a pseudo-random gaussian distributed number between the given min and max with the provided standard deviation.
	 *
	 * @param min  the minimum bound
	 * @param max  the maximum bound
	 * @param mean the mean value
	 * @param sd   the standard deviation from the mean
	 * @return a gaussian distributed number between the provided bounds
	 */
	public static int nextGaussian(final int min, final int max, final int mean, final double sd) {
		return min + Math.abs(((int) (nextGaussian() * sd + mean)) % (max - min));
	}
}
