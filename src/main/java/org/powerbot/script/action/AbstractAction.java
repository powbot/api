package org.powerbot.script.action;

import org.powerbot.bot.rt4.client.internal.IClient;

import java.util.concurrent.Future;

public abstract class AbstractAction<T> {
	private String entityName;
	private String interaction;
	private int mouseX;
	private int mouseY;
	private IClient client;
	private boolean async;

	public String getEntityName() {
		return entityName;
	}

	public T setEntityName(String entityName) {
		this.entityName = entityName;
		return (T) this;
	}

	public String getInteraction() {
		return interaction;
	}

	public T setInteraction(String interaction) {
		this.interaction = interaction;
		return (T) this;
	}

	public int getMouseX() {
		return mouseX;
	}

	public T setMouseX(int mouseX) {
		this.mouseX = mouseX;
		return (T) this;
	}

	public int getMouseY() {
		return mouseY;
	}

	public T setMouseY(int mouseY) {
		this.mouseY = mouseY;
		return (T) this;
	}

	public IClient getClient() {
		return client;
	}

	public T setClient(IClient client) {
		this.client = client;
		return (T) this;
	}

	public boolean isAsync() {
		return async;
	}

	public T setAsync(boolean async) {
		this.async = async;
		return (T) this;
	}
}
