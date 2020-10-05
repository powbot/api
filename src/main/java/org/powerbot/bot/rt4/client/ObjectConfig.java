package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IObjectConfig;

public class ObjectConfig extends Proxy<IObjectConfig> {

	public ObjectConfig(IObjectConfig wrapped) {
		super(wrapped);
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
