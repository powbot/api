package org.powerbot.bot.rt4.client.internal;

import org.powerbot.script.ClientContext;
import org.powerbot.script.Identifiable;
import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Actor;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Player;

public interface IProjectile extends IRenderable, Identifiable, Locatable {

	int getCycleEnd();

	int getCycleStart();

	int getEndHeight();

	int getId();

	int getOrientation();

	int getPlane();

	int getSlope();

	int getStartDistance();

	int getStartX();

	int getStartY();

	int getTargetIndex();

	double getX();

	double getY();

	boolean isStarted();

	@Override
	default int id() {
		return getId();
	}

	@Override
	default Tile tile() {
		return new Tile(getWorldX(), getWorldY(), ClientContext.ctx().client().getFloor());
	}
	default int getWorldX() {
		int x = (int) getX();
		x = (x >> 7) + org.powerbot.script.rt4.ClientContext.ctx().game.mapOffset().x();
		return x;
	}

	default int getWorldY() {
		int y = (int) getY();
		y = (y >> 7) + org.powerbot.script.rt4.ClientContext.ctx().game.mapOffset().y();
		return y;
	}

	default int getWorldStartX() {
		int x = getStartX();
		x = (x >> 7) + org.powerbot.script.rt4.ClientContext.ctx().game.mapOffset().x();
		return x;
	}

	default int getWorldStartY() {
		int y = getStartY();
		y = (y >> 7) + org.powerbot.script.rt4.ClientContext.ctx().game.mapOffset().y();
		return y;
	}

	default int getWorldStartZ() {
		return getPlane();
	}

	default Actor getTarget() {
		final org.powerbot.script.rt4.ClientContext ctx = org.powerbot.script.rt4.ClientContext.ctx();
		int index = getTargetIndex();

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
}
