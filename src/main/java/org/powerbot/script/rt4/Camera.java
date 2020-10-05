package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.*;

/**
 * Camera
 * A utility class with methods to retrieve camera values and control the mouse and keyboard to manipulate the viewport camera.
 */
public class Camera extends ClientAccessor {
	public final float[] offset;
	public final float[] center;

	public Camera(final ClientContext factory) {
		super(factory);
		offset = new float[3];
		center = new float[3];
	}

	/**
	 * Returns the camera offset on the x-axis.
	 *
	 * @return the offset on the x-axis
	 */
	public int x() {
		final Client client = ctx.client();
		return client.getCameraX();
	}

	/**
	 * Returns the camera offset on the y-axis.
	 *
	 * @return the offset on the y-axis
	 */
	public int y() {
		final Client client = ctx.client();
		return client.getCameraY();
	}

	/**
	 * Returns the camera offset on the z-axis.
	 *
	 * @return the offset on the z-axis
	 */
	public int z() {
		final Client client = ctx.client();
		return client.getCameraZ();
	}

	/**
	 * Determines the current camera yaw (angle of rotation).
	 *
	 * @return the camera yaw
	 */
	public int yaw() {
		final Client client = ctx.client();
		return (int) (client.getCameraYaw() / 5.68);
	}

	/**
	 * Determines the current camera pitch.
	 *
	 * @return the camera pitch
	 */
	public final int pitch() {
		final Client client = ctx.client();
		return (int) ((client.getCameraPitch() - 128) / 2.56);
	}

	/**
	 * Sets the camera pitch to one absolute, up or down.
	 *
	 * @param up {@code true} to be up; otherwise {@code false} for down
	 * @return {@code true} if the absolute was reached; success is normally guaranteed regardless of return of {@code false}
	 */
	public boolean pitch(final boolean up) {
		return pitch(up ? 100 : 0);
	}

	/**
	 * Sets the camera pitch the desired percentage.
	 *
	 * @param percent the percent to set the pitch to
	 * @return {@code true} if the pitch was reached; otherwise {@code false}
	 */
	public boolean pitch(final int percent) {
		if (percent == pitch()) {
			return true;
		}
		final boolean up = pitch() < percent;
		ctx.input.send(up ? "{VK_UP down}" : "{VK_DOWN down}");
		for (; ; ) {
			final int tp = pitch();
			if (!Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					return pitch() != tp;
				}
			}, 10, 10)) {
				break;
			}
			final int p = pitch();
			if (up && p >= percent) {
				break;
			} else if (!up && p <= percent) {
				break;
			}
		}
		ctx.input.send(up ? "{VK_UP up}" : "{VK_DOWN up}");
		return Math.abs(percent - pitch()) <= 8;
	}

	/**
	 * Changes the yaw (angle) of the camera.
	 *
	 * @param direction the direction to set the camera, 'n', 's', 'w', 'e'.     \
	 * @return {@code true} if the camera was rotated to the angle; otherwise {@code false}
	 */
	public boolean angle(final char direction) {
		switch (direction) {
		case 'n':
			return angle(0);
		case 'w':
			return angle(90);
		case 's':
			return angle(180);
		case 'e':
			return angle(270);
		}
		throw new RuntimeException("invalid direction " + direction + ", expecting n,w,s,e");
	}

	/**
	 * Changes the yaw (angle) of the camera.
	 *
	 * @param degrees the degrees to set the camera to
	 * @return {@code true} if the camera was rotated to the angle; otherwise {@code false}
	 */
	public boolean angle(final int degrees) {
		final int d = degrees % 360;
		final int a = angleTo(d);
		if (Math.abs(a) <= 5) {
			return true;
		}
		final boolean l = a > 5;

		ctx.input.send(l ? "{VK_LEFT down}" : "{VK_RIGHT down}");
		final float dir = Math.signum(angleTo(d));
		for (; ; ) {
			final int a2 = angleTo(d);
			if (!Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					return angleTo(d) != a2;
				}
			}, 10, 10)) {
				break;
			}
			final int at = angleTo(d);
			if (Math.abs(at) <= 15 || Math.signum(at) != dir) {
				break;
			}
		}
		ctx.input.send(l ? "{VK_LEFT up}" : "{VK_RIGHT up}");
		return Math.abs(angleTo(d)) <= 15;
	}

	/**
	 * Gets the angle change to the specified degrees.
	 *
	 * @param degrees the degrees to compute to
	 * @return the angle change required to be at the provided degrees
	 */
	public int angleTo(final int degrees) {
		int ca = yaw();
		if (ca < degrees) {
			ca += 360;
		}
		int da = ca - degrees;
		if (da > 180) {
			da -= 360;
		}
		return da;
	}

	/**
	 * Turns to the specified {@link org.powerbot.script.Locatable}.
	 *
	 * @param l the {@link org.powerbot.script.Locatable} to turn to
	 */
	public void turnTo(final Locatable l) {
		turnTo(l, 0);
	}

	/**
	 * Turns to the specified {@link org.powerbot.script.Locatable} with the provided deviation.
	 *
	 * @param l   the {@link org.powerbot.script.Locatable} to turn to
	 * @param dev the yaw deviation
	 */
	public void turnTo(final Locatable l, final int dev) {
		final int a = angleToLocatable(l);
		if (dev == 0) {
			angle(a);
		} else {
			angle(Random.nextInt(a - dev, a + dev + 1));
		}
	}

	public int angleToLocatable(final Locatable mobile) {
		final Player local = ctx.players.local();
		final Tile t1 = local != null ? local.tile() : null;
		final Tile t2 = mobile != null ? mobile.tile() : null;
		return t1 != null && t2 != null ? ((int) Math.toDegrees(Math.atan2(t2.y() - t1.y(), t2.x() - t1.x()))) - 90 : 0;
	}
}
