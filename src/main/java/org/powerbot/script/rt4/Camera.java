package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.script.*;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Camera
 * A utility class with methods to retrieve camera values and control the mouse and keyboard to manipulate the viewport camera.
 */
public class Camera extends ClientAccessor {
	public final float[] offset;
	public final float[] center;

	public static final double ZOOM_MAX = 4.33;
	public static final double ZOOM_MIN = 0.54;

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
		final IClient client = ctx.client();
		return client.getCameraX();
	}

	/**
	 * Returns the camera offset on the y-axis.
	 *
	 * @return the offset on the y-axis
	 */
	public int y() {
		final IClient client = ctx.client();
		return client.getCameraY();
	}

	/**
	 * Returns the camera offset on the z-axis.
	 *
	 * @return the offset on the z-axis
	 */
	public int z() {
		final IClient client = ctx.client();
		return client.getCameraZ();
	}

	/**
	 * Determines the current camera yaw (angle of rotation).
	 *
	 * @return the camera yaw
	 */
	public int yaw() {
		final IClient client = ctx.client();
		return (int) (client.getCameraYaw() / 5.68);
	}

	/**
	 * Determines the current camera pitch.
	 *
	 * @return the camera pitch
	 */
	public final int pitch() {
		final IClient client = ctx.client();
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
		if (ctx.client().isMobile()) {
			turnTo(degrees, pitch());
		} else {
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
		}
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
	 * Returns the screen point vector required to change the camera from the start to the end positions.
	 *
	 * @param startYaw   The starting camera yaw.
	 * @param startPitch The starting camera pitch.
	 * @param endYaw     The ending camera yaw.
	 * @param endPitch   The ending camera pitch.
	 * @return The screen point vector.
	 */
	private Point getCameraVector(int startYaw, int startPitch, int endYaw, int endPitch) {
		double cPitch = endPitch - startPitch; // Defines the change in pitch required.

		// Yaw is a periodic feature [0, 359]. The change in pitch may differ across the end-point of the cycle.
		double cYawCyclA = (endYaw + 360) - startYaw; // Forwards across the 360 endpoint.
		double cYawCyclB = endYaw - (startYaw + 360); // Backwards across the 360 endpoint.
		double cYawNorm = endYaw - startYaw; // Change in-between [0, 359], no endpoint crossed.

		double cYaw; // Defines the shortest change in yaw required.

		// Compare the absolute differences to determine the shortest path.
		if (Math.abs(cYawNorm) < Math.abs(cYawCyclA) && Math.abs(cYawNorm) < Math.abs(cYawCyclB)) {
			cYaw = cYawNorm;
		} else if (Math.abs(cYawCyclA) < Math.abs(cYawNorm) && Math.abs(cYawCyclA) < Math.abs(cYawCyclB)) {
			cYaw = cYawCyclA;
		} else {
			cYaw = cYawCyclB;
		}

		// Define the screen vector components. Math.PI and (4/3) represent calculated slopes.
		int changeX = (int) ((-Math.PI) * cYaw);
		int changeY = (int) ((4d / 3) * cPitch);

		return new Point(changeX, changeY);
	}

	/**
	 * Turn the in-game camera to the specificed yaw and pitch.
	 *
	 * @param yaw   The desired yaw.
	 * @param pitch The desired pitch.
	 */
	public void turnTo(int yaw, int pitch) {
		final java.util.Random r = new java.util.Random();

		final org.powerbot.script.rt4.Camera camera = this.ctx.camera;
		final Input input = this.ctx.input;
		final Point vector = this.getCameraVector(camera.yaw(), camera.pitch(), yaw, pitch); // Retrieve the screen vector for the camera turn.

		// Define the min and max screen boundaries in which the vector can be dragged.
		int minX, minY, maxX, maxY;
		if (ctx.client().isMobile()) {
			final Component viewport = ctx.game.mobileViewport();
			final Point p = viewport.basePoint();
			minX = p.x;
			minY = p.y;
			maxX = p.x + viewport.width();
			maxY = p.x + viewport.height();
		} else {
			minX = minY = 0; // By default, we assume that the base of the boundary starts at 0.


			maxX = ctx.client().getCanvas().getWidth(); // By default, we assume that the max of the boundary end with the screen.
			maxY = ctx.client().getCanvas().getHeight();
		}

		// Adjust the boundaries based on the values of the components in the screen vector.
		if (vector.x < 0) {
			minX = -vector.x;
		} else {
			maxX -= vector.x;
		}

		if (vector.y < 0) {
			minY = -vector.y;
		} else {
			maxY -= vector.y;
		}

		// Defines the range of X and Y.
		final int xRange = maxX - minX;
		final int yRange = maxY - minY;

		// Finds a random starting point to start the drag.
		final int randX = minX + (xRange > 0 ? r.nextInt(xRange) : 0);
		final int randY = minY + (yRange > 0 ? r.nextInt(yRange) : 0);

		// Drags the camera from the starting point by the screen vector amount.

		input.move(new Point(randX, randY));
		input.drag(new Point(randX + vector.x, randY + vector.y), MouseEvent.BUTTON2);
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

	/**
	 * Get the zoom of the client as a value betweem 0-100 (0 == zoomed out, 100 == zoomed in)
	 *
	 * @return zoom
	 */
	public int getZoom() {
		final boolean resizable = ctx.game.resizable();
		final double height = resizable ? ctx.game.dimensions().height : (Game.FIXED_VIEWPORT_END_Y - Game.FIXED_VIEWPORT_START_Y);
		final double zoom = ctx.client().getTileSize();

		double zoomReal = (Math.round((zoom / height) * 100.0) / 100.0) - ZOOM_MIN;
		return (int) ((zoomReal / (ZOOM_MAX - ZOOM_MIN)) * 100.0);
	}
}
