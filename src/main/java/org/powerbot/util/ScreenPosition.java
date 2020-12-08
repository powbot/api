package org.powerbot.util;

import org.powerbot.script.InteractiveEntity;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;
import java.util.concurrent.Callable;

/**
 * @author rvbiljouw
 */
public final class ScreenPosition {

	private ScreenPosition() {
	}

	public static Callable<Point> of(final ClientContext ctx, InteractiveEntity entity) {
		return new Callable<>() {
			private Tile lastPlayerTile;
			private Tile lastEntityTile;
			private int lastCameraX;
			private int lastCameraY;
			private int lastCameraZ;
			private Point lastTarget;

			@Override
			public Point call() {
				final Tile currentTile = entity.tile();
				final Tile playerTile = ctx.players.local().tile();
				if (!currentTile.equals(lastEntityTile)
					|| !playerTile.equals(lastPlayerTile)
					|| lastCameraX != ctx.camera.x()
					|| lastCameraY != ctx.camera.y()
					|| lastCameraZ != ctx.camera.z()) {

					lastCameraX = ctx.camera.x();
					lastCameraY = ctx.camera.y();
					lastCameraZ = ctx.camera.z();
					lastEntityTile = currentTile;
					lastPlayerTile = playerTile;
					lastTarget = entity.nextPoint();
				}
				return lastTarget;
			}
		};
	}

}
