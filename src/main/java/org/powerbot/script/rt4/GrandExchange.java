package org.powerbot.script.rt4;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.StringUtils;

import java.awt.*;

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
		final int customQuantityButton = 7;
		final Component chatInput = ctx.widgets.component(CHAT_INPUT, CHAT_INPUT_TEXT);
		if (Integer.parseInt(quantityComponent.text()) != quantity) {
			ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID,
				GRAND_EXCHANGE_CREATE_OFFER_COMPONENT,customQuantityButton).click();
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
		final int customPriceButton = 12;
		final Component chatInput = ctx.widgets.component(CHAT_INPUT, CHAT_INPUT_TEXT);
		if (StringUtils.filterCoinsText(priceComponent.text()) != price) {
			ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID,
				GRAND_EXCHANGE_CREATE_OFFER_COMPONENT, customPriceButton).click();
			if (Condition.wait(() -> chatInput.visible() && chatInput.text().contains("Set a price for each item"), 500, 5)) {
				Condition.sleep(Random.nextInt(900, 1400));
				ctx.input.sendln(Integer.toString(price));
				Condition.wait(() -> price == StringUtils.filterCoinsText(priceComponent.text()), 500, 5);
			}
		}
		return StringUtils.filterCoinsText(priceComponent.text()) == price;
	}

	public boolean collectOffer(GeSlot slot) {
		final int CF_COLLECTION_ITEMS = 23;
		final int C_COLLECTION_ITEM_ONE = 2;
		final int C_COLLECTION_ITEM_TWO = 3;
		Component itemOne, itemTwo;

		slot.getComponent().interact("View offer");
		Condition.wait(() -> ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID, CF_COLLECTION_ITEMS).visible(), 600, 5);
		itemOne = ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID, CF_COLLECTION_ITEMS, C_COLLECTION_ITEM_ONE);
		itemTwo = ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID, CF_COLLECTION_ITEMS, C_COLLECTION_ITEM_TWO);

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
		if (slot == null) {
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

	public boolean confirmOffer(int itemId, GeSlot slot) {
		final int confirm = 54;
		ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID, GRAND_EXCHANGE_CREATE_OFFER_COMPONENT, confirm).click();
		return Condition.wait(() -> slot.getItemId() == itemId, 500, 5);
	}

	/**
	 * cancels an offer that is being made
	 * @return returns true if no longer in create offer window
	 */
	public boolean cancelOffer() {
		final int button = 4;
		Component component = ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID, button);
		component.click();
		return Condition.wait(() -> !component.visible(), 600, 5);
	}

	public boolean startOffer(GeSlot slot, int itemId, String itemName, boolean buy) {
		Component itemComponent = null;
		if (buy) {
			slot.getComponent().component(0).click();
			Component chatItemInput = ctx.widgets.component(CHAT_INPUT, CHAT_INPUT_ITEM);
			Component searchResults = ctx.widgets.component(CHAT_INPUT, 53);
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
			for (Component co : ctx.widgets.component(GRAND_EXCHANGE_INVENTORY_WIDGET_ID, 0).components()) {
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

		return Condition.wait(() -> ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID, GRAND_EXCHANGE_CREATE_OFFER_COMPONENT,21).itemId() == itemId, 500, 5);
	}

	private boolean scrollToComponent(Rectangle view, Component component) {
		Point scrollPoint = new Point(-1, -1);
		final Component scrollBar = ctx.widgets.component(162, 54, 1);

		while (component.valid() && !view.contains(component.centerPoint()) &&
			!scrollPoint.equals(scrollBar.screenPoint())) {
			scrollPoint = scrollBar.screenPoint();
			ctx.input.move(new Point(Random.nextInt(view.x + 10, view.x + view.width - 10), Random.nextInt(view.y + 10, view.y + view.height - 10)));
			ctx.input.scroll(true);
			Condition.sleep(Random.nextGaussian(100, 200, 120));
			ctx.input.scroll(true);
		}
		return view.contains(component.centerPoint());
	}

	private Component getBuyItemComponent(int itemId) {
		final int searchResultsFolder = 53;
		for (Component component : ctx.widgets.component(CHAT_INPUT, searchResultsFolder).components()) {
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
		return ctx.client().isMembers() ? GRAND_EXCHANGE_FIRST_OFFER_SLOT + 8 : GRAND_EXCHANGE_FIRST_OFFER_SLOT + 3;
	}

	public GeSlot[] getAllSlots() {
		GeSlot[] slots = new GeSlot[(ctx.client().isMembers() ? 8 : 3)];
		int j = 0;
		for (int i = GRAND_EXCHANGE_FIRST_OFFER_SLOT; i <= (getLastOfferIndex()); i++) {
			slots[j++] = new GeSlot(ctx.widgets.component(GRAND_EXCHANGE_WIDGET_ID, i));
			if (slots[j - 1].getItemId() == -1) {
				slots[j - 1] = GeSlot.NIL;
			}
		}
		return slots;
	}

	public boolean collectAllToBank() {
		Component button = ctx.widgets.component(
			GRAND_EXCHANGE_WIDGET_ID,
			GRAND_EXCHANGE_COLLECT_BUTTON_COMPONENT,
			GRAND_EXCHANGE_COLLECT_BUTTON_CHILD
		);
		button.interact("Collect to bank");
		return Condition.wait(button::visible, 500, 5);
	}

	public boolean collectAllToInventory() {
		Component button = ctx.widgets.component(
			GRAND_EXCHANGE_WIDGET_ID,
			GRAND_EXCHANGE_COLLECT_BUTTON_COMPONENT,
			GRAND_EXCHANGE_COLLECT_BUTTON_CHILD
		);
		button.interact("Collect to inventory");
		return Condition.wait(button::	visible, 500, 5);
	}

	public boolean open() {
		if (opened()) {
			return true;
		}

		Npc npc = ctx.npcs.toStream().action("Exchange").nearest().first();
		npc.interact("Exchange");
		return Condition.wait(this::opened, 500, 5);
	}

	public boolean close() {
		Component button = ctx.components.toStream().widget(GRAND_EXCHANGE_WIDGET_ID).texture(CLOSE_BUTTON_TEXTURES).first();
		button.interact("Close");
		return Condition.wait(() -> !opened(), 500, 5);
	}

	public boolean opened() {
		return ctx.widgets.widget(GRAND_EXCHANGE_WIDGET_ID).valid();
	}
}
