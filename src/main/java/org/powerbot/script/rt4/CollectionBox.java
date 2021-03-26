package org.powerbot.script.rt4;

import org.powerbot.script.Condition;

import java.util.ArrayList;

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

	private boolean collectCoins(CollectionSlot slot, boolean toBank) {
		Component item = slot.getComponent().component(COLLECTION_BOX_ITEM_TWO);
		if (toBank) {
			item.interact("Bank");
		} else {
			item.click();
		}
		return Condition.wait(() -> !item.visible(), 500, 5);
	}

	private boolean collectItems(CollectionSlot slot, boolean toBank) {
		Component item = slot.getComponent().component(COLLECTION_BOX_ITEM_ONE);
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
		if (npc.interact("Collect")) {
			return Condition.wait(this::opened, 500, 5);
		}
		return false;
	}

	public boolean close() {
		Component button = ctx.components.toStream().widget(COLLECTION_BOX_WIDGET_ID).texture(CLOSE_BUTTON_TEXTURES).first();
		if (button.interact("Close")) {
			return Condition.wait(() -> !opened(), 500, 5);
		}
		return false;
	}

	public boolean opened() {
		return ctx.widgets.widget(COLLECTION_BOX_WIDGET_ID).valid();
	}

	public boolean collectToInventory() {
		Component button = ctx.widgets.component(COLLECTION_BOX_WIDGET_ID, COLLECTION_BOX_TO_INVENTORY_BUTTON_COMPONENT, COLLECTION_BOX_TO_INVENTORY_BUTTON_CHILD);
		if (button.textColor() == CAN_COLLECT_ITEM_TEXT_COLOR) {
			button.click();
		}
		return Condition.wait(() -> button.textColor() != CAN_COLLECT_ITEM_TEXT_COLOR, 500, 5);
	}

	public boolean collectToBank() {
		Component button = ctx.widgets.component(COLLECTION_BOX_WIDGET_ID, COLLECTION_BOX_TO_BANK_BUTTON_COMPONENT, COLLECTION_BOX_TO_BANK_BUTTON_CHILD);
		if (button.textColor() == CAN_COLLECT_ITEM_TEXT_COLOR) {
			button.click();
		}
		return Condition.wait(() -> button.textColor() != CAN_COLLECT_ITEM_TEXT_COLOR, 500, 5);
	}

	private int getLastOfferSlot() {
		return (ctx.client().isMembers() ? COLLECTION_BOX_FIRST_OFFER_SLOT + GRAND_EXCHANGE_P2P_SLOTS : COLLECTION_BOX_FIRST_OFFER_SLOT + GRAND_EXCHANGE_F2P_SLOTS);
	}

	public ArrayList<CollectionSlot> getAllSlots() {
		ArrayList<CollectionSlot> slots = new ArrayList<>();
		for (int i = COLLECTION_BOX_FIRST_OFFER_SLOT; i <= getLastOfferSlot(); i++) {
			CollectionSlot s = new CollectionSlot(ctx.widgets.component(COLLECTION_BOX_WIDGET_ID, i));
			if (s.getItemId() == -1) {
				s = CollectionSlot.NIL;
			}
			slots.add(s);
		}
		return slots;
	}
}
