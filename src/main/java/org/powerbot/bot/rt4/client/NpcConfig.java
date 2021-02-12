package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.INpcConfig;

public class NpcConfig extends Proxy<INpcConfig> {

	public NpcConfig(final INpcConfig wrapped) {
		super(wrapped);
	}

	public int getId() {
		if (!isNull()) {
			return get().getId();
		}

		return -1;
	}

	public int getLevel() {
		if (!isNull()) {
			return get().getLevel();
		}

		return -1;
	}

	public String getName() {
		if (!isNull()) {
			return get().getName();
		}

		return null;
	}

	public String[] getActions() {
		if (!isNull()) {
			return get().getActions();
		}

		return null;
	}

	public int[] getConfigs() {
		if (!isNull()) {
			return get().getConfigs();
		}

		return null;
	}

	public int getVarpbitIndex() {
		if (!isNull()) {
			return get().getVarpbitIndex();
		}

		return -1;
	}

	public int getVarbit() {
		if (!isNull()) {
			return get().getVarbit();
		}

		return -1;
	}
}
