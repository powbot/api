package org.powerbot.script.rt4;

import org.powerbot.script.Condition;

public class CollectionSlot {
	private final Component component;
	private String name = null;

	public final static CollectionSlot NIL = new CollectionSlot();

	private CollectionSlot() {
		component = Component.NIL;
	}

	CollectionSlot(GeSlot slot) {
		component = ClientContext.ctx().widgets.component(Constants.COLLECTION_BOX_WIDGET_ID,
			slot.getComponent().index() - Constants.GRAND_EXCHANGE_FIRST_OFFER_SLOT + Constants.COLLECTION_BOX_FIRST_OFFER_SLOT);
	}

	CollectionSlot(Component component) {
		this.component = component;
	}

	/**
	 * hovers slot to get name
	 * @return returns the name displayed from progress info when hovering over component
	 */
	public String getItemName() {
		if (name != null) {
			return name;
		}
		if (this.equals(NIL) || getItemId() == -1) {
			return "";
		}

		Component progressInfo = ClientContext.ctx().widgets.component(Constants.COLLECTION_BOX_WIDGET_ID,13, 2);
		component.hover();
		if (!Condition.wait(progressInfo::visible, 500, 5)) {
			return "";
		}
		return progressInfo.text().substring(progressInfo.text().indexOf(": ") + 2, progressInfo.text().indexOf("<"));
	}

	public int getItemId() {
		return component.component(21).itemId();
	}

	public int getQuantity() {
		return component.component(21).itemStackSize();
	}

	public boolean isBuy() {
		final int buyTextureId = 1118;
		return component.component(19).textureId() == buyTextureId;
	}

	/**
	 * hovers slot to get progress info
	 * @return returns the amount of items sold or bought so far
	 */
	public int getProgress() {
		if (this.equals(NIL) || getItemId() == -1) {
			return -1;
		}

		Component progressInfo = ClientContext.ctx().widgets.component(Constants.COLLECTION_BOX_WIDGET_ID,13,2);
		component.hover();
		if (!Condition.wait(progressInfo::visible, 500, 5)) {
			return -1;
		}
		return Integer.parseInt(progressInfo.text().substring(progressInfo.text().indexOf(">") + 1, progressInfo.text().indexOf(" /")));
	}

	public boolean isFinished() {
		final int completedTextColor = 24320;
		return component.component(15).textColor() == completedTextColor;
	}

	public GrandExchangeItem getGeItem() {
		if (getItemId() != -1) {
			return new GrandExchangeItem(getItemName());
		}

		return GrandExchangeItem.NIL;
	}

	public boolean isAborted() {
		final int abortedTextColor = 9371648;
		return component.component(15).textColor() == abortedTextColor;
	}

	public Component getComponent() {
		return component;
	}
}
