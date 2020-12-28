package org.powerbot.script.action;

public abstract class AbstractAction {
	private String entityName;
	private String interaction;
	private int mouseX;
	private int mouseY;

	public String getEntityName() {
		return entityName;
	}

	public AbstractAction setEntityName(String entityName) {
		this.entityName = entityName;
		return this;
	}

	public String getInteraction() {
		return interaction;
	}

	public AbstractAction setInteraction(String interaction) {
		this.interaction = interaction;
		return this;
	}

	public int getMouseX() {
		return mouseX;
	}

	public AbstractAction setMouseX(int mouseX) {
		this.mouseX = mouseX;
		return this;
	}

	public int getMouseY() {
		return mouseY;
	}

	public AbstractAction setMouseY(int mouseY) {
		this.mouseY = mouseY;
		return this;
	}
}
