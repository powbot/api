package org.powerbot.bot.rt4.client.internal;

public interface IPlayer extends IActor {

	int getCombatLevel();

	IPlayerComposite getComposite();

	IStringRecord getName();

	int getTeam();

}