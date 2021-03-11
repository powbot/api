package org.powerbot.bot.rt4.client.internal;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;
import java.util.concurrent.Callable;

public interface IPlayer extends IActor {

	int getCombatLevel();

	IPlayerComposite getComposite();

	IStringRecord getName();

	int getTeam();

	@Override
	default String[] actions() {
		return new String[0];
	}

	@Override
	default int id() {
		return -1;
	}

	@Override
	default String name() {
		final String str = getName() != null ? getName().getValue() : "";
		return str != null ? str : "";
	}

	@Override
	default int[] modelIds() {
		return null;
	}
}
