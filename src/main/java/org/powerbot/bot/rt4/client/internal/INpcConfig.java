package org.powerbot.bot.rt4.client.internal;

public interface INpcConfig extends IEntry {

	String[] getActions();

	int[] getConfigs();

	int getId();

	int getLevel();

	String getName();

	int getVarbit();

	int getVarpbitIndex();

}