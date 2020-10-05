package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.INpcConfig;

public class NpcConfig extends Proxy<INpcConfig> {

	public NpcConfig(final INpcConfig wrapped) {
		super(wrapped);
	}

	public int getId() {
		if (!isNull()) {
			return wrapped.get().getId();
		}

		return -1;
	}

	public int getLevel() {
		if (!isNull()) {
			return wrapped.get().getLevel();
		}

		return -1;
	}

	public String getName() {
		if (!isNull()) {
			return wrapped.get().getName();
		}

		return null;
	}

	public String[] getActions() {
		if (!isNull()) {
			return wrapped.get().getActions();
		}

		return null;
	}

	public int[] getConfigs() {
		if (!isNull()) {
			return wrapped.get().getConfigs();
		}

		return null;
	}

	public int getVarpbitIndex() {
		if (!isNull()) {
			return wrapped.get().getVarpbitIndex();
		}

		return -1;
	}

	public int getVarbit() {
		if (!isNull()) {
			return wrapped.get().getVarbit();
		}

		return -1;
	}
}
