package org.powerbot.script.rt4;

import org.powerbot.script.Condition;
import org.powerbot.script.StringUtils;

import static org.powerbot.script.rt4.Constants.*;

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
		return component.component(GE_SLOT_ITEM_NAME).text();
	}

	public int getItemId() {
		return component.component(GE_SLOT_ITEM).itemId();
	}

	public boolean isAvailable() {
		return component.component(GE_SLOT_BUY_OFFER_STATE).text().equals("Empty");
	}

	public int getQuantity() {
		return component.component(GE_SLOT_ITEM).itemStackSize();
	}

	public boolean isBuy() {
		return component.component(GE_SLOT_BUY_OFFER_STATE).text().equals("Buy");
	}

	public int getPricePerItem() {
		return StringUtils.parseCoinsAmount(component.component(GE_SLOT_PRICE_PER_ITEM).text());
	}

	/**
	 * hovers slot to get progress info
	 * @return returns the amount of items sold or bought so far
	 */
	public int getProgress() {
		if (this.equals(NIL) || isAvailable()) {
			return -1;
		}

		Component progressInfo = ClientContext.ctx().widgets.component(
			Constants.GRAND_EXCHANGE_WIDGET_ID,
			GE_SLOT_PROGRESS_INFO_COMPONENT,
			GE_SLOT_PROGRESS_INFO_CHILD);

		if (component.hover()) {
			Condition.wait(progressInfo::visible, 500, 5);
			return Integer.parseInt(progressInfo.text().substring(progressInfo.text().indexOf(">") + 1, progressInfo.text().indexOf(" /")));
		}
		return -1;
	}

	public boolean isFinished() {
		return component.component(GE_SLOT_PROGRESS_BAR).textColor() == GE_FINISHED_TEXT_COLOR;
	}

	public GrandExchangeItem getGrandExchangeItem() {
		if (!isAvailable()) {
			return new GrandExchangeItem(getItemName());
		}

		return GrandExchangeItem.NIL;
	}

	public boolean isAborted() {
		return component.component(GE_SLOT_PROGRESS_BAR).textColor() == GE_ABORTED_TEXT_COLOR;
	}

	public CollectionSlot collectionSlot() {
		return new CollectionSlot(this);
	}

	public Component getComponent() {
		return component;
	}
}
