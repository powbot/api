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
		return projectile.getId();
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
		return new Tile(getX(), getY(), ctx.client().getFloor());
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
		int x = (int) projectile.getX();
		x = (x >> 7) + ctx.game.mapOffset().x();
		return x;
	}

	public int getY() {
		int y = (int) projectile.getY();
		y = (y >> 7) + ctx.game.mapOffset().y();
		return y;
	}

	public int getStartX() {
		int x = projectile.getStartX();
		x = (x >> 7) + ctx.game.mapOffset().x();
		return x;
	}

	public int getStartY() {
		int y = projectile.getStartY();
		y = (y >> 7) + ctx.game.mapOffset().y();
		return y;
	}

	public int getStartZ() {
		return projectile.getPlane();
	}

	public Actor getTarget() {
		int index = projectile.getTargetIndex();

		final Actor nil = ctx.npcs.nil();
		final IClient client = ctx.client();
		if (client == null) {
			return nil;
		}
		if (index > 0) {
			final INpc[] npcs = client.getNpcs();
			return index < npcs.length ? new Npc(ctx, npcs[index - 1]) : nil;
		} else if(index < 0){
			index = -index;
			if (index == client.getPlayerIndex()) {
				return new Player(ctx, client.getPlayer());
			}
			final IPlayer[] players = client.getPlayers();
			return index < players.length ? new Player(ctx, players[index - 1]) : nil;
		} else {
			return nil;
		}
	}

	@Override
	public Projectile nil() {
		return NIL;
	}
}
