package org.powerbot.script.rt4;

import org.powerbot.script.*;

import java.awt.*;
import java.util.Arrays;

/**
 * BoundingModel
 * A closed 3D shape with methods for converting to 2D and performing various checks for containment and finding internal points.
 */
public abstract class BoundingModel extends ClientAccessor {
	private final Vector3 start, end;

	public BoundingModel(final ClientContext ctx, final Vector3 start, final Vector3 end) {
		this(ctx, start.x, end.x, start.y, end.y, start.z, end.z);
	}

	public BoundingModel(final ClientContext ctx, final int x1, final int x2, final int y1, final int y2, final int z1, final int z2) {
		super(ctx);
		start = new Vector3(
				Math.min(x1, x2),
				Math.min(y1, y2),
				Math.min(z1, z2)
		);
		end = new Vector3(
				Math.max(x1, x2),
				Math.max(y1, y2),
				Math.max(z1, z2)
		);
	}

	public abstract int x();

	public abstract int z();

	Point centroid(final int index) {
		final int[][][] triangles = project();
		if (index < 0 || index >= triangles.length) {
			return new Point(-1, -1);
		}
		final int x = x(), z = z(), y = ctx.game.tileHeight(x, z);
		final Point p = ctx.game.worldToScreen(
				x + (triangles[index][0][0] + triangles[index][1][0] + triangles[index][2][0]) / 3,
				y + (triangles[index][0][1] + triangles[index][1][1] + triangles[index][2][1]) / 3,
				z + (triangles[index][0][2] + triangles[index][1][2] + triangles[index][2][2]) / 3,
				0
		);
		return ctx.game.inViewport(p) ? p : new Point(-1, -1);
	}

	public Point nextPoint() {
		final int[][][] triangles = project();
		final int faces = triangles.length;
		final int mark = Random.nextInt(0, faces);
		Point point = firstInViewportCentroid(triangles, mark, faces);
		return point != null ? point : (point = firstInViewportCentroid(triangles, 0, mark)) != null ? point : new Point(-1, -1);
	}

	public Point centerPoint() {
		final int[][][] triangles = project();
		final int faces = triangles.length;
		int avgX = 0;
		int avgY = 0;
		int avgZ = 0;
		int index = 0;
		final int x = x(), z = z(), y = ctx.game.tileHeight(x, z);
		while (index < faces) {
			avgX += (triangles[index][0][0] + triangles[index][1][0] + triangles[index][2][0]) / 3;
			avgY += (triangles[index][0][1] + triangles[index][1][1] + triangles[index][2][1]) / 3;
			avgZ += (triangles[index][0][2] + triangles[index][1][2] + triangles[index][2][2]) / 3;
			index++;
		}
		final Point p = ctx.game.worldToScreen(
				x + avgX / faces,
				y + avgY / faces,
				z + avgZ / faces,
				0
		);
		return ctx.game.inViewport(p) ? p : new Point(-1, -1);
	}

	public boolean contains(final Point p) {
		final int[][][] triangles = project();
		final int px = p.x, py = p.y;
		final int x = x(), z = z(), y = ctx.game.tileHeight(x, z);
		loop:
		for (final int[][] triangle : triangles) {
			final Point[] arr = {
					ctx.game.worldToScreen(x + triangle[0][0], y + triangle[0][1], z + triangle[0][2], 0),
					ctx.game.worldToScreen(x + triangle[1][0], y + triangle[1][1], z + triangle[1][2], 0),
					ctx.game.worldToScreen(x + triangle[2][0], y + triangle[2][1], z + triangle[2][2], 0),
			};
			for (final Point p2 : arr) {
				if (!ctx.game.inViewport(p2)) {
					continue loop;
				}
			}
			if (barycentric(px, py, arr[0].x, arr[0].y, arr[1].x, arr[1].y, arr[2].x, arr[2].y)) {
				return true;
			}
		}
		return false;
	}

	public boolean drawWireFrame(final Graphics graphics) {
		final int[][][] triangles = project();
		final int x = x(), z = z(), y = ctx.game.tileHeight(x, z);
		loop:
		for (final int[][] triangle : triangles) {
			final Point[] arr = {
					ctx.game.worldToScreen(x + triangle[0][0], y + triangle[0][1], z + triangle[0][2], 0),
					ctx.game.worldToScreen(x + triangle[1][0], y + triangle[1][1], z + triangle[1][2], 0),
					ctx.game.worldToScreen(x + triangle[2][0], y + triangle[2][1], z + triangle[2][2], 0),
			};
			for (final Point p2 : arr) {
				if (!ctx.game.inViewport(p2)) {
					continue loop;
				}
			}
			graphics.drawLine(arr[0].x, arr[0].y, arr[1].x, arr[1].y);
			graphics.drawLine(arr[1].x, arr[1].y, arr[2].x, arr[2].y);
			graphics.drawLine(arr[2].x, arr[2].y, arr[0].x, arr[0].y);
		}
		return false;
	}

	public Polygon[] triangles() {
		final int[][][] triangles = project();
		final int x = x(), z = z(), y = ctx.game.tileHeight(x, z);
		final Polygon[] polygons = new Polygon[triangles.length];
		int count = 0;
		loop:
		for (final int[][] triangle : triangles) {
			final Point[] arr = {
					ctx.game.worldToScreen(x + triangle[0][0], y + triangle[0][1], z + triangle[0][2], 0),
					ctx.game.worldToScreen(x + triangle[1][0], y + triangle[1][1], z + triangle[1][2], 0),
					ctx.game.worldToScreen(x + triangle[2][0], y + triangle[2][1], z + triangle[2][2], 0),
			};
			for (final Point p2 : arr) {
				if (!ctx.game.inViewport(p2)) {
					continue loop;
				}
			}
			polygons[count++] = new Polygon(
					new int[]{arr[0].x, arr[1].x, arr[2].x},
					new int[]{arr[0].y, arr[1].y, arr[2].y},
					3
			);
		}
		return Arrays.copyOf(polygons, count);
	}

	private int firstInViewportIndex(final int[][][] triangles, final int pos, final int length) {
		final int x = x(), z = z(), y = ctx.game.tileHeight(x, z);
		int index = pos;
		loop:
		while (index < length) {
			final int[][] triangle = triangles[index++];
			final Point[] arr = {
					ctx.game.worldToScreen(x + triangle[0][0], y + triangle[0][1], z + triangle[0][2], 0),
					ctx.game.worldToScreen(x + triangle[1][0], y + triangle[1][1], z + triangle[1][2], 0),
					ctx.game.worldToScreen(x + triangle[2][0], y + triangle[2][1], z + triangle[2][2], 0),
			};
			for (final Point p2 : arr) {
				if (!ctx.game.inViewport(p2)) {
					continue loop;
				}
			}
			return index - 1;
		}
		return -1;
	}

	private Point firstInViewportCentroid(final int[][][] triangles, final int pos, final int length) {
		final int index = firstInViewportIndex(triangles, pos, length);
		return index != -1 ? centroid(index) : null;
	}

	private boolean barycentric(final int x, final int y, final int aX, final int aY, final int bX, final int bY, final int cX, final int cY) {
		final int v00 = cX - aX;
		final int v01 = cY - aY;
		final int v10 = bX - aX;
		final int v11 = bY - aY;
		final int v20 = x - aX;
		final int v21 = y - aY;
		final int d00 = v00 * v00 + v01 * v01;
		final int d01 = v00 * v10 + v01 * v11;
		final int d02 = v00 * v20 + v01 * v21;
		final int d11 = v10 * v10 + v11 * v11;
		final int d12 = v10 * v20 + v11 * v21;
		final float denom = 1.0f / (d00 * d11 - d01 * d01);
		final float u = (d11 * d02 - d01 * d12) * denom;
		final float v = (d00 * d12 - d01 * d02) * denom;
		return u >= 0 && v >= 0 && u + v < 1;
	}

	private int[][][] project() {
		final Vector3[] verticies = {
				new Vector3(start.x, start.y, start.z),
				new Vector3(start.x, start.y, end.z),
				new Vector3(end.x, start.y, end.z),
				new Vector3(end.x, start.y, start.z),
				new Vector3(start.x, end.y, start.z),
				new Vector3(start.x, end.y, end.z),
				new Vector3(end.x, end.y, end.z),
				new Vector3(end.x, end.y, start.z),
		};
		final int[][] sides = {
				{0, 1, 2, 3},//BOTTOM
				{4, 5, 6, 7},//TOP
				{1, 5, 6, 2},//FRONT
				{3, 7, 4, 0},//BACK
				{0, 4, 5, 1},//L
				{2, 6, 7, 3},//R
		};
		final int[][] triangles = {
				{0, 1, 3},
				{2, 3, 1},
		};
		int faces = 0;
		final int[][][] model = new int[sides.length * triangles.length][3][3];
		for (final int[] side : sides) {
			for (final int[] triangle : triangles) {
				final Vector3 v1 = verticies[side[triangle[0]]], v2 = verticies[side[triangle[1]]], v3 = verticies[side[triangle[2]]];
				model[faces][0] = v1.toMatrix();
				model[faces][1] = v2.toMatrix();
				model[faces++][2] = v3.toMatrix();
			}
		}
		return Arrays.copyOf(model, faces);
	}
}
