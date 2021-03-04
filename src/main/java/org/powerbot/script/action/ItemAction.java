package org.powerbot.script.action;

public class ItemAction extends AbstractAction<ItemAction> {
	private int itemId;
	private int slot;
	private int widgetId;

	public int getItemId() {
		return itemId;
	}

	public ItemAction setItemId(int itemId) {
		this.itemId = itemId;
		return this;
	}

	public int getSlot() {
		return slot;
	}

	public ItemAction setSlot(int slot) {
		this.slot = slot;
		return this;
	}

	public int getWidgetId() {
		return widgetId;
	}

	public ItemAction setWidgetId(int widgetId) {
		this.widgetId = widgetId;
		return this;
	}
}
