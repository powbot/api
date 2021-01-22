package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.*;

/**
 * Projectile
 */
public class Projectile extends ClientAccessor implements Locatable, Identifiable, Validatable {
	private final org.powerbot.bot.rt4.client.Projectile projectile;

	public Projectile(final ClientContext ctx, final org.powerbot.bot.rt4.client.Projectile projectile) {
		super(ctx);
		this.projectile = projectile;
	}

	@Override
	public int id() {
		return projectile.getId();
	}

	@Override
	public boolean valid() {
		return !projectile.isNull() && ctx.projectiles.select().contains(this);
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
		final Client client = ctx.client();
		if (client == null) {
			return nil;
		}
		if (index > 0) {
			final org.powerbot.bot.rt4.client.Npc[] npcs = client.getNpcs();
			return index < npcs.length ? new Npc(ctx, npcs[index - 1]) : nil;
		} else if(index < 0){
			index = -index;
			if (index == client.getPlayerIndex()) {
				return new Player(ctx, client.getPlayer());
			}
			final org.powerbot.bot.rt4.client.Player[] players = client.getPlayers();
			return index < players.length ? new Player(ctx, players[index - 1]) : nil;
		} else {
			return nil;
		}
	}

}
