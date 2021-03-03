package org.powerbot.script.action;

public class WidgetAction extends AbstractAction<WidgetAction> {
	private int id;
	private int slot;

	public int getId() {
		return id;
	}

	public WidgetAction setId(int id) {
		this.id = id;
		return this;
	}

	public int getSlot() {
		return slot;
	}

	public WidgetAction setSlot(int slot) {
		this.slot = slot;
		return this;
	}

}
