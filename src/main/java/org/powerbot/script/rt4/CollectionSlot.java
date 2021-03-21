package org.powerbot.script.rt4;

import org.powerbot.script.Condition;

import static org.powerbot.script.rt4.Constants.*;

public class CollectionSlot {
	private final Component component;
	private String name = null;

	public final static CollectionSlot NIL = new CollectionSlot();

	private CollectionSlot() {
		component = Component.NIL;
	}

	public CollectionSlot(GeSlot slot) {
		component = ClientContext.ctx().widgets.component(COLLECTION_BOX_WIDGET_ID,
			slot.getComponent().index() - GRAND_EXCHANGE_FIRST_OFFER_SLOT + COLLECTION_BOX_FIRST_OFFER_SLOT);
	}

	public CollectionSlot(Component component) {
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

		Component progressInfo = ClientContext.ctx().widgets.component(COLLECTION_BOX_WIDGET_ID, COLLECTION_BOX_PROGRESS_COMPONENT, COLLECTION_BOX_PROGRESS_CHILD);
		component.hover();
		if (!Condition.wait(progressInfo::visible, 500, 5)) {
			return "";
		}
		return progressInfo.text().substring(progressInfo.text().indexOf(": ") + 2, progressInfo.text().indexOf("<"));
	}

	public int getItemId() {
		return component.component(COLLECTION_BOX_SLOT_ITEM).itemId();
	}

	public int getQuantity() {
		return component.component(COLLECTION_BOX_SLOT_ITEM).itemStackSize();
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

		Component progressInfo = Component progressInfo = ClientContext.ctx().widgets.component(COLLECTION_BOX_WIDGET_ID, COLLECTION_BOX_PROGRESS_COMPONENT, COLLECTION_BOX_PROGRESS_CHILD);
		component.hover();
		if (!Condition.wait(progressInfo::visible, 500, 5)) {
			return -1;
		}
		return Integer.parseInt(progressInfo.text().substring(progressInfo.text().indexOf(">") + 1, progressInfo.text().indexOf(" /")));
	}

	public boolean isFinished() {
		return component.component(COLLECTION_BOX_PROGRESS_BAR).textColor() == GE_FINISHED_TEXT_COLOR;
	}

	public GrandExchangeItem getGrandExchangeItem() {
		if (getItemId() != -1) {
			return new GrandExchangeItem(getItemName());
		}

		return GrandExchangeItem.NIL;
	}

	public boolean isAborted() {
		return component.component().textColor() == GE_ABORTED_TEXT_COLOR;
	}

	public Component getComponent() {
		return component;
	}
}
