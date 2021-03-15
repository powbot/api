package org.powerbot.script.rt4;

import org.powerbot.bot.model.QuickHuller;
import org.powerbot.bot.rt4.client.internal.IModel;
import org.powerbot.script.Random;
import org.powerbot.script.Vector3;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Model {

	private ClientContext ctx;

	private int[] verticesX;
	private int[] verticesY;
	private int[] verticesZ;
	private int[] indicesX;
	private int[] indicesY;
	private int[] indicesZ;

	private int[] originalVerticesX;
	private int[] originalVerticesZ;
	private int[] originalIndicesX;
	private int[] originalIndicesZ;

	private int orientation;

	public Model(final int[] verticesX, final int[] verticesY, final int[] verticesZ,
				 final int[] indicesX, final int[] indicesY, final int[] indicesZ, final int orientation) {
		this(org.powerbot.script.ClientContext.ctx(), verticesX, verticesY, verticesZ,
			indicesX, indicesY, indicesZ, orientation);
	}

	public Model(final ClientContext ctx, final int[] verticesX, final int[] verticesY, final int[] verticesZ,
				 final int[] indicesX, final int[] indicesY, final int[] indicesZ, final int orientation) {
		this.ctx = ctx;
		this.verticesX = verticesX;
		this.verticesY = verticesY;
		this.verticesZ = verticesZ;
		this.indicesX = indicesX;
		this.indicesY = indicesY;
		this.indicesZ = indicesZ;
		this.orientation = ((orientation & 0x3FFF) + 1024) % 2048;

		save();
	}

	private void save() {
		this.originalVerticesX = verticesX.clone();
		this.originalVerticesZ = verticesZ.clone();
		this.originalIndicesX = indicesX.clone();
		this.originalIndicesZ = indicesZ.clone();
	}

	public Model update(final int[] verticesX, final int[] verticesY, final int[] verticesZ,
				 final int[] indicesX, final int[] indicesY, final int[] indicesZ, final int orientation) {
		this.verticesX = verticesX;
		this.verticesY = verticesY;
		this.verticesZ = verticesZ;
		this.indicesX = indicesX;
		this.indicesY = indicesY;
		this.indicesZ = indicesZ;
		this.orientation = ((orientation & 0x3FFF) + 1024) % 2048;

		save();

		return this;
	}

	/**
	 * Gets all the polygons of the model
	 * @param localX the local x of the entity
	 * @param localY the local y of the entity
	 * @return a list of polygons
	 */
	public List<Polygon> polygons(final int localX, final int localY) {
		setOrientation();

		final int[] indX = indicesX();
		final int[] indY = indicesY();
		final int[] indZ = indicesZ();

		final int[] vertX = verticesX();
		final int[] vertY = verticesY();
		final int[] vertZ = verticesZ();

		final List<Polygon> polys = new ArrayList<>();
		final boolean resizable = ctx.game.resizable();
		for (int i = 0; i < indX.length; i++) {
			if (i >= indY.length && i >= indZ.length) {
				return null;
			}

			int xIdx = indX[i];
			int yIdx = indY[i];
			int zIdx = indZ[i];
			if (xIdx >= vertX.length || xIdx >= vertY.length || xIdx >= vertZ.length ||
				yIdx >= vertX.length || yIdx >= vertY.length || yIdx >= vertZ.length ||
				zIdx >= vertX.length || zIdx >= vertY.length || zIdx >= vertZ.length) {
				return null;
			}
			final Point x = ctx.game.worldToScreen(
				localX - vertX[xIdx],
				localY - vertZ[xIdx],
				-vertY[xIdx],
				resizable
			);
			final Point y = ctx.game.worldToScreen(
				localX - vertX[yIdx],
				localY - vertZ[yIdx],
				-vertY[yIdx],
				resizable
			);
			final Point z = ctx.game.worldToScreen(
				localX - vertX[zIdx],
				localY - vertZ[zIdx],
				-vertY[zIdx],
				resizable
			);

			if (ctx.game.inViewport(x, resizable) && ctx.game.inViewport(y, resizable) && ctx.game.inViewport(z, resizable)) {
				polys.add(new Polygon(
					new int[]{x.x, y.x, z.x},
					new int[]{x.y, y.y, z.y},
					3));
			}
		}

		return polys;
	}

	/**
	 * Gets all the polygons of the model
	 * @param localX the local x of the entity
	 * @param localY the local y of the entity
	 * @param g graphics object to onRender with
	 */
	public void draw(final int localX, final int localY, final Graphics g) {
		for (final Polygon polygon : polygons(localX, localY)) {
			g.drawPolygon(polygon);
		}
	}

	public Point nextPoint(final int localX, final int localY) {
		final List<Polygon> polygons = polygons(localX, localY);
		if (polygons == null || polygons.isEmpty()) {
			return new Point(-1, -1);
		}

		// Select random triangle of model
		final Polygon triangle = polygons.get(Random.nextInt(0, polygons.size() - 1));
		final Point[] pts = {
			new Point(triangle.xpoints[0], triangle.ypoints[0]),
			new Point(triangle.xpoints[1], triangle.ypoints[1]),
			new Point(triangle.xpoints[2], triangle.ypoints[2])
		};

		// Compute random point within triangle
		final double r1 = Math.random(), r2 = Math.random();
		final double sqrtR1 = Math.sqrt(r1);
		final double sqrtR1R2 = sqrtR1 * r2;
		final double x = (1 - sqrtR1) * pts[0].x + (sqrtR1 * (1 - r2)) * pts[1].x + sqrtR1R2 * pts[2].x;
		final double y = (1 - sqrtR1) * pts[0].y + (sqrtR1 * (1 - r2)) * pts[1].y + sqrtR1R2 * pts[2].y;

		return new Point((int) x, (int) y);
	}

	public boolean contains(final Point point, final int localX, final int localY) {
		final List<Polygon> polygons = polygons(localX, localY);
		return polygons != null && !polygons.isEmpty() && polygons.stream().anyMatch(polygon -> polygon.contains(point));
	}

	private void setOrientation() {
		if (orientation == 0)
			return;

		final int sin = Game.ARRAY_SIN[orientation];
		final int cos = Game.ARRAY_COS[orientation];
		for (int i = 0; i < verticesX.length; ++i) {
			if (originalVerticesX.length <= i || originalVerticesZ.length <= i) {
				break;
			}

			verticesX[i] = originalVerticesX[i] * cos + originalVerticesZ[i] * sin >> 16;
			verticesZ[i] = originalVerticesZ[i] * cos - originalVerticesX[i] * sin >> 16;
		}
	}

	public Vector3[][] vectors() {
		final Vector3[][] vectors = new Vector3[indicesX().length][3];

		final int[] indX = indicesX();
		final int[] indY = indicesY();
		final int[] indZ = indicesZ();

		final int[] vertX = verticesX();
		final int[] vertY = verticesY();
		final int[] vertZ = verticesZ();

		for (int i = 0; i < vectors.length; i++) {
			vectors[i][0] = new Vector3(vertX[indX[i]], vertX[indY[i]], vertX[indZ[i]]);
			vectors[i][1] = new Vector3(vertY[indX[i]], vertY[indY[i]], vertY[indZ[i]]);
			vectors[i][2] = new Vector3(vertZ[indX[i]], vertZ[indY[i]], vertZ[indZ[i]]);
		}
		return vectors;
	}

	public List<Point> points(final int localX, final int localY) {
		return polygons(localX, localY).stream().map(p -> {
			final int x = IntStream.of(p.xpoints).sum() / p.npoints;
			final int y = IntStream.of(p.ypoints).sum() / p.npoints;

			return new Point(x, y);
		}).collect(Collectors.toList());
	}

	public Point centerPoint(final int localX, final int localY) {
		final boolean isResizable = ctx.game.resizable();
		final List<Point> points = points(localX, localY);
		if (points.size() == 0) {
			return new Point(-1, -1);
		}
		final int xTotal = points.stream().mapToInt(p -> p.x).sum();
		final int yTotal = points.stream().mapToInt(p -> p.y).sum();
		final Point central = new Point(xTotal / points.size(), yTotal / points.size());

		List<Point> orderedPoints = points.stream().filter(p -> ctx.game.inViewport(p, isResizable)).sorted((a, b) -> {
			double distA = Math.sqrt(((central.x - a.x) * (central.x - a.x)) + ((central.y - a.y) * (central.y - a.y)));
			double distB = Math.sqrt(((central.x - b.x) * (central.x - b.x)) + ((central.y - b.y) * (central.y - b.y)));

			if (distB > distA) {
				return -1;
			} else if (distB == distA) {
				return 0;
			}
			return 1;
		}).collect(Collectors.toList());

		if (orderedPoints.size() > 0) {
			return orderedPoints.get(0);
		}

		return new Point(-1, -1);
	}

	public void mirrorModel() {
		for(int i = 0; i < this.originalVerticesZ.length; ++i) {
			this.originalVerticesZ[i] = -this.originalVerticesZ[i];
		}

		for(int i = 0; i < this.originalIndicesX.length; ++i) {
			final int oldX = this.originalIndicesX[i];
			this.originalIndicesX[i] = this.originalIndicesZ[i];
			this.originalIndicesZ[i] = oldX;
		}
	}

	public void rotate(final int num) {
		for (int n = 0; n < num; n++) {
			for (int i = 0; i < originalVerticesX.length; ++i) {
				final int oldX = originalVerticesX[i];
				originalVerticesX[i] = originalVerticesZ[i];
				originalVerticesZ[i] = -oldX;
			}
		}

		this.verticesX = originalVerticesX;
		this.verticesZ = originalVerticesZ;
	}

	public int[] verticesX() {
		return verticesX;
	}

	public int[] verticesY() {
		return verticesY;
	}

	public int[] verticesZ() {
		return verticesZ;
	}

	public int[] indicesX() {
		return indicesX;
	}

	public int[] indicesY() {
		return indicesY;
	}

	public int[] indicesZ() {
		return indicesZ;
	}

	@Override
	public String toString() {
		return "Model{" +
			"verticesX=" + verticesX.length +
			", verticesY=" + verticesY.length +
			", verticesZ=" + verticesZ.length +
			", indicesX=" + indicesX.length +
			", indicesY=" + indicesY.length +
			", indicesZ=" + indicesZ.length +
			'}';
	}

	public Polygon quickHull(final int localX, final int localY) {
		return QuickHuller.INSTANCE.hull(points(localX, localY));
	}

	public void setContext(final ClientContext ctx) {
		this.ctx = ctx;
	}
}
