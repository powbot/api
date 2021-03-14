package org.powerbot.script.rt4;

import org.powerbot.script.Condition;

import static org.powerbot.script.rt4.Constants.*;

public class CollectionBox extends ClientAccessor {

	public CollectionBox(ClientContext ctx) {
		super(ctx);
	}

	public boolean collectOffer(CollectionSlot slot, boolean toBank) {
		if (!collectCoins(slot, toBank)) {
			return false;
		}
		return collectItems(slot, toBank);
	}

	public boolean collectCoins(CollectionSlot slot, boolean toBank) {
		Component item = slot.getComponent().component(4);
		if (toBank) {
			item.interact("Bank");
		} else {
			item.click();
		}
		return Condition.wait(() -> !item.visible(), 500, 5);
	}

	public boolean collectItems(CollectionSlot slot, boolean toBank) {
		Component item = slot.getComponent().component(3);
		if (toBank) {
			item.interact("Bank");
		} else {
			item.click();
		}
		return Condition.wait(() -> !item.visible(), 500, 5);
	}

	public boolean open() {
		if (opened()) {
			return true;
		}

		Npc npc = ctx.npcs.toStream().action("Collect").nearest().first();
		npc.interact("Collect");
		return Condition.wait(this::opened, 500, 5);
	}

	public boolean close() {
		Component button = ctx.widgets.component(COLLECTION_BOX_WIDGET_ID, 2, 11);
		button.interact("Close");
		return Condition.wait(() -> !opened(), 500, 5);
	}

	public boolean opened() {
		return ctx.widgets.widget(COLLECTION_BOX_WIDGET_ID).valid();
	}

	public boolean collectToInventory() {
		Component button = ctx.widgets.component(COLLECTION_BOX_WIDGET_ID, 3, 1);
		if (button.textColor() == CAN_COLLECT_ITEM_TEXT_COLOR) {
			button.click();
		}
		return Condition.wait(() -> button.textColor() != CAN_COLLECT_ITEM_TEXT_COLOR, 500, 5);
	}

	public boolean collectToBank() {
		Component button = ctx.widgets.component(COLLECTION_BOX_WIDGET_ID, 4, 1);
		if (button.textColor() == CAN_COLLECT_ITEM_TEXT_COLOR) {
			button.click();
		}
		return Condition.wait(() -> button.textColor() != CAN_COLLECT_ITEM_TEXT_COLOR, 500, 5);
	}

	private int getLastOfferSlot() {
		return (ctx.client().isMembers() ? COLLECTION_BOX_FIRST_OFFER_SLOT + 8 : COLLECTION_BOX_FIRST_OFFER_SLOT + 3);
	}

	public CollectionSlot[] getAllSlots() {
		CollectionSlot[] slots = new CollectionSlot[(ctx.client().isMembers() ? 8 : 3)];
		int j = 0;
		for (int i = COLLECTION_BOX_FIRST_OFFER_SLOT; i <= getLastOfferSlot(); i++) {
			slots[j++] = new CollectionSlot(ctx.widgets.component(COLLECTION_BOX_WIDGET_ID, i));
			if (slots[j - 1].getItemId() == -1) {
				slots[j - 1] = CollectionSlot.NIL;
			}
		}
		return slots;
	}
}
