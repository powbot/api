package org.powerbot.script.rt4;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.StringUtils;

import java.awt.*;
import java.util.ArrayList;

import static org.powerbot.script.rt4.Constants.*;

public class GrandExchange extends ClientAccessor {

	public GrandExchange(ClientContext ctx) {
		super(ctx);
	}

	public boolean setQuantity(int quantity) {
		final Component quantityComponent = ctx.widgets.component(
			GRAND_EXCHANGE_WIDGET_ID,
			GRAND_EXCHANGE_CREATE_OFFER_COMPONENT,
			GRAND_EXCHANGE_CREATE_OFFER_QUANTITY
		);
		final Component chatInput = ctx.widgets.component(CHAT_INPUT, CHAT_INPUT_TEXT);
		if (Integer.parseInt(quantityComponent.text()) != quantity) {
			ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID,
				GRAND_EXCHANGE_CREATE_OFFER_COMPONENT,GRAND_EXCHANGE_CUSTOM_QUANTITY_BUTTON).click();
			if (Condition.wait(() -> chatInput.visible() && chatInput.text().contains("How many do you wish to"), 500, 5)) {
				ctx.input.sendln(Integer.toString(quantity));
				Condition.wait(() -> Integer.parseInt(quantityComponent.text()) == quantity, 500, 5);
			}
		}
		return Integer.parseInt(quantityComponent.text()) == quantity;
	}

	public boolean setPrice(int price) {
		final Component priceComponent = ctx.widgets.component(
			GRAND_EXCHANGE_WIDGET_ID,
			GRAND_EXCHANGE_CREATE_OFFER_COMPONENT,
			GRAND_EXCHANGE_CREATE_OFFER_PRICE
		);
		final Component chatInput = ctx.widgets.component(CHAT_INPUT, CHAT_INPUT_TEXT);
		if (StringUtils.parseCoinsAmount(priceComponent.text()) != price) {
			ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID,
				GRAND_EXCHANGE_CREATE_OFFER_COMPONENT, GRAND_EXCHANGE_CUSTOM_PRICE_BUTTON).click();
			if (Condition.wait(() -> chatInput.visible() && chatInput.text().contains("Set a price for each item"), 500, 5)) {
				Condition.sleep(Random.nextInt(900, 2400));
				ctx.input.sendln(Integer.toString(price));
				Condition.wait(() -> price == StringUtils.parseCoinsAmount(priceComponent.text()), 500, 5);
			}
		}
		return StringUtils.parseCoinsAmount(priceComponent.text()) == price;
	}

	public boolean collectOffer(GeSlot slot) {
		if (!slot.getComponent().interact("View offer")) {
			return false;
		}
		Condition.wait(() -> ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID, GRAND_EXCHANGE_COLLECT_OFFER_ITEMS_COMPONENT).visible(), 500, 5);

		Component itemOne, itemTwo;
		itemOne = ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID, GRAND_EXCHANGE_COLLECT_OFFER_ITEMS_COMPONENT, GRAND_EXCHANGE_COLLECT_OFFER_ITEM_ONE);
		itemTwo = ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID, GRAND_EXCHANGE_COLLECT_OFFER_ITEMS_COMPONENT, GRAND_EXCHANGE_COLLECT_OFFER_ITEM_TWO);

		if (itemOne.itemId() != -1) {
			itemOne.interact("Collect");
		}
		if (itemTwo.itemId() != -1) {
			itemTwo.interact("Collect");
		}

		return (Condition.wait(slot::isAvailable, 600, 5));
	}

	public boolean abortOffer(GeSlot slot) {
		slot.getComponent().interact("Abort offer");
		return  (Condition.wait(slot::isAborted, 600, 5));
	}

	public GeSlot createOffer(GrandExchangeItem geItem, int quantity, int price, boolean buy) {
		GeSlot slot = getAvailableSlot();
		if (slot == null || slot.equals(GeSlot.NIL)) {
			return GeSlot.NIL;
		}
		if (!startOffer(slot, geItem.getId(), geItem.getName(), buy)) {
			cancelOffer();
			return GeSlot.NIL;
		}
		if (!setQuantity(quantity)) {
			cancelOffer();
			return GeSlot.NIL;
		}
		if (!setPrice(price)) {
			cancelOffer();
			return GeSlot.NIL;
		}

		if (!confirmOffer(geItem.getId(), slot)) {
			cancelOffer();
			return GeSlot.NIL;
		}

		return slot;
	}

	/**
	 *
	 * @param itemId item id of item made offer for
	 * @param slot the ge slot made offer on
	 * @return returns true only if the correct itemId is found at the correct GeSlot
	 */
	public boolean confirmOffer(int itemId, GeSlot slot) {
		final int confirm = GRAND_EXCHANGE_CONFIRM_OFFER_BUTTON;
		if ( ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID, GRAND_EXCHANGE_CREATE_OFFER_COMPONENT, GRAND_EXCHANGE_CONFIRM_OFFER_BUTTON).click()) {
			return Condition.wait(() -> slot.getItemId() == itemId, 500, 5);
		}
		return false;
	}

	/**
	 * cancels an offer that is being made
	 * @return returns true if no longer in create offer window
	 */
	public boolean cancelOffer() {
		Component component = ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID, GRAND_EXCHANGE_CANCEL_BUTTON);
		if (component.click()) {
			return Condition.wait(() -> !component.visible(), 600, 5);
		}
		return false;
	}

	public boolean startOffer(GeSlot slot, int itemId, String itemName, boolean buy) {
		Component itemComponent = null;
		if (buy) {
			slot.getComponent().component(GRAND_EXCHANGE_BUY_BUTTON).click();
			Component chatItemInput = ctx.widgets.component(CHAT_INPUT, CHAT_INPUT_ITEM);
			Component searchResults = ctx.widgets.component(CHAT_INPUT, GRAND_EXCHANGE_SEARCH_RESULT_COMPONENT);
			if (Condition.wait(() -> chatItemInput.visible() && chatItemInput.text().contains("would you like to buy"), 500, 5)) {
				ctx.input.send(itemName);

				Condition.wait(() -> getBuyItemComponent(itemId) != null, 500, 5);
				itemComponent = getBuyItemComponent(itemId);

				if (itemComponent != null && !searchResults.boundingRect().contains(itemComponent.centerPoint())) {
					if (!scrollToComponent(searchResults.boundingRect(), itemComponent)) {
						return false;
					}
				}
			}

		} else {
			for (Component co : ctx.widgets.component(GRAND_EXCHANGE_INVENTORY_WIDGET_ID, GRAND_EXCHANGE_INVENTORY_COMPONENT).components()) {
				if (co.itemId() == itemId) {
					itemComponent = co;
					break;
				}
			}
		}

		if (itemComponent == null) {
			return false;
		}

		itemComponent.hover();
		itemComponent.interact(true, (buy ? "Select" : "Offer"), itemName);

		return Condition.wait(() -> ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID,
			GRAND_EXCHANGE_CREATE_OFFER_COMPONENT,GRAND_EXCHANGE_CREATE_OFFER_ITEM).itemId() == itemId, 500, 5);
	}

	private boolean scrollToComponent(Rectangle view, Component component) {
		Point scrollPoint = new Point(-1, -1);
		final Component scrollBar = ctx.widgets.component(CHAT_INPUT, GRAND_EXCHANGE_SEARCH_RESULT_SCROLL_BAR_COMPONENT, GRAND_EXCHANGE_SEARCH_RESULT_SCROLL_BAR_CHILD);

		while (component.valid() && !view.contains(component.centerPoint()) &&
			!scrollPoint.equals(scrollBar.screenPoint())) {
			scrollPoint = scrollBar.screenPoint();
			ctx.input.move(new Point(Random.nextInt(view.x + 10, view.x + view.width - 10), Random.nextInt(view.y + 10, view.y + view.height - 10)));
			ctx.input.scroll(true);
			Condition.sleep(Random.nextGaussian(300, 700, 420));
			ctx.input.scroll(true);
		}
		return view.contains(component.centerPoint());
	}

	private Component getBuyItemComponent(int itemId) {
		for (Component component : ctx.widgets.component(CHAT_INPUT, GRAND_EXCHANGE_SEARCH_RESULT_COMPONENT).components()) {
			if (component.itemId() == itemId && component.visible()) {
				return component;
			}
		}
		return null;
	}

	public GeSlot getAvailableSlot() {
		for (int i = GRAND_EXCHANGE_FIRST_OFFER_SLOT; i <= getLastOfferIndex(); i++) {
			GeSlot slot = new GeSlot(ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID, i));
			if (slot.isAvailable()) {
				return slot;
			}
		}
		return GeSlot.NIL;
	}

	private int getLastOfferIndex() {
		return ctx.client().isMembers() ? GRAND_EXCHANGE_FIRST_OFFER_SLOT + GRAND_EXCHANGE_P2P_SLOTS : GRAND_EXCHANGE_FIRST_OFFER_SLOT + GRAND_EXCHANGE_F2P_SLOTS;
	}

	public ArrayList<GeSlot> getAllSlots() {
		ArrayList<GeSlot> slots = new ArrayList<>();
		for (int i = GRAND_EXCHANGE_FIRST_OFFER_SLOT; i <= (getLastOfferIndex()); i++) {
			GeSlot s = new GeSlot(ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID, i));
			if (s.getItemId() == -1) {
				s = GeSlot.NIL;
			}
			slots.add(s);
		}
		return slots;
	}

	public boolean collectAllToBank() {
		Component button = ctx.widgets.component(
			GRAND_EXCHANGE_WIDGET_ID,
			GRAND_EXCHANGE_COLLECT_BUTTON_COMPONENT,
			GRAND_EXCHANGE_COLLECT_BUTTON_CHILD
		);
		if (button.interact("Collect to bank")) {
			return Condition.wait(button::visible, 500, 5);
		}
		return false;
	}

	public boolean collectAllToInventory() {
		Component button = ctx.widgets.component(
			GRAND_EXCHANGE_WIDGET_ID,
			GRAND_EXCHANGE_COLLECT_BUTTON_COMPONENT,
			GRAND_EXCHANGE_COLLECT_BUTTON_CHILD
		);
		if (button.interact("Collect to inventory")) {
			return Condition.wait(button::	visible, 500, 5);
		}
		return false;
	}

	public boolean open() {
		if (opened()) {
			return true;
		}

		Npc npc = ctx.npcs.toStream().action("Exchange").nearest().first();
		if (npc.interact("Exchange")) {
			return Condition.wait(this::opened, 500, 5);
		}
		return false;
	}

	public boolean close() {
		Component button = ctx.components.toStream().widget(GRAND_EXCHANGE_WIDGET_ID).texture(CLOSE_BUTTON_TEXTURES).first();
		if (button.interact("Close")) {
			return Condition.wait(() -> !opened(), 500, 5);
		}
		return false;
	}

	public boolean opened() {
		return ctx.widgets.widget(GRAND_EXCHANGE_WIDGET_ID).valid();
	}
}
