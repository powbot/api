package org.powerbot.script.rt4;

import org.powerbot.script.Condition;
import org.powerbot.script.StringUtils;

public class GeSlot {
	private final Component component;

	public final static GeSlot NIL = new GeSlot();

	private GeSlot() {
		component = Component.NIL;
	}

	public GeSlot(Component component) {
		this.component = component;
	}

	public String getItemName() {
		return component.component(19).text();
	}

	public int getItemId() {
		return component.component(18).itemId();
	}

	public boolean isAvailable() {
		return component.component(16).text().equals("Empty");
	}

	public int getQuantity() {
		return component.component(18).itemStackSize();
	}

	public boolean isBuy() {
		return component.component(16).text().equals("Buy");
	}

	public int getPricePerItem() {
		return StringUtils.filterCoinsText(component.component(25).text());
	}

	/**
	 * hovers slot to get progress info
	 * @return returns the amount of items sold or bought so far
	 */
	public int getProgress() {
		if (this.equals(NIL) || isAvailable()) {
			return -1;
		}

		Component progressInfo = ClientContext.ctx().widgets.component(Constants.GRAND_EXCHANGE_INVENTORY_WIDGET_ID,29, 2).component(2);
		component.hover();
		if (!Condition.wait(progressInfo::visible, 500, 5)) {
			return -1;
		}
		return Integer.parseInt(progressInfo.text().substring(progressInfo.text().indexOf(">") + 1, progressInfo.text().indexOf(" /")));
	}

	public boolean isFinished() {
		final int completedTextColor = 24320;
		return component.component(22).textColor() == completedTextColor;
	}

	public GrandExchangeItem getGeItem() {
		if (!isAvailable()) {
			return new GrandExchangeItem(getItemName());
		}

		return GrandExchangeItem.NIL;
	}

	public boolean isAborted() {
		final int abortedTextColor = 9371648;
		return component.component(22).textColor() == abortedTextColor;
	}

	public CollectionSlot collectionSlot() {
		return new CollectionSlot(this);
	}

	public Component getComponent() {
		return component;
	}
}
