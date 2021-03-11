package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.internal.INpc;
import org.powerbot.bot.rt4.client.internal.IPlayer;
import org.powerbot.bot.rt4.client.internal.IProjectile;
import org.powerbot.script.*;

/**
 * Projectile
 */
public class Projectile extends ClientAccessor implements Locatable, Identifiable, Validatable, Nillable<Projectile> {
	private final IProjectile projectile;

	public static final Projectile NIL = new Projectile(org.powerbot.script.ClientContext.ctx(), null);

	public Projectile(final ClientContext ctx, final IProjectile projectile) {
		super(ctx);
		this.projectile = projectile;
	}

	@Override
	public int id() {
		return projectile.id();
	}

	@Override
	public boolean valid() {
		return projectile != null && ctx.projectiles.toStream().contains(this);
	}

	@Override
	public int hashCode() {
		return this.projectile.hashCode();
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof Projectile && projectile.equals(((Projectile) o).projectile);
	}

	@Override
	public Tile tile() {
		if (!valid()) {
			return Tile.NIL;
		}
		return projectile != null ? projectile.tile() : Tile.NIL;
	}

	public boolean isStarted() {
		return projectile.isStarted();
	}

	public int getCycleStart() {
		return projectile.getCycleStart();
	}

	public int getEndHeight() {
		return projectile.getEndHeight();
	}

	public int getStartDistance() {
		return projectile.getStartDistance();
	}

	public int getOrientation() {
		return projectile.getOrientation();
	}

	public int getSlope() {
		return projectile.getSlope();
	}

	public int getId() {
		return projectile.getId();
	}

	public int getX() {
		return projectile != null ? projectile.getWorldX() : -1;
	}

	public int getY() {
		return projectile != null ? projectile.getWorldY() : -1;
	}

	public int getStartX() {
		return projectile != null ? projectile.getWorldStartX() : -1;
	}

	public int getStartY() {
		return projectile != null ? projectile.getWorldStartY() : -1;
	}

	public int getStartZ() {
		return projectile != null ? projectile.getWorldStartZ() : -1;
	}

	public Actor getTarget() {
		return projectile != null ? projectile.getTarget() : ctx.npcs.nil();
	}

	@Override
	public Projectile nil() {
		return NIL;
	}
}
