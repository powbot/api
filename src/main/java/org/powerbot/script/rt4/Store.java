package org.powerbot.script.rt4;

import org.powerbot.script.Condition;

import java.util.Arrays;
import static org.powerbot.script.rt4.Constants.*;
import static org.powerbot.script.rt4.Constants.STORE_QUANTITY_FIFTY_COMPONENT_ID;

public class Store extends ClientAccessor {

	private String ACTION_TRADE = "Trade";
	private String ACTION_CLOSE = "Close";

	public Store(ClientContext ctx) {
		super(ctx);
	}

	/**
	 *  Checks if a store if currently open
	 * @return true if store is open
	 */
	public boolean opened() {
		return getStoreWidget().valid();
	}

	/**
	 *  Opens the store by talking to a nearby NPC
	 * @param npcName Name of NPC to open the store
	 * @return  Returns true if opened
	 */
	public boolean open(String npcName) {
		Npc npc = ctx.npcs.toStream().name(npcName).action(ACTION_TRADE).nearest().first();

		if (npc.valid() && npc.interact(ACTION_TRADE)) {
			return Condition.wait(this::opened);
		}

		return false;
	}

	/**
	 *  Closes the store by pressing the top right button
	 * @return True if store is closed.
	 */
	public boolean close() {
		if (!opened()) {
			return true;
		}

		Component component = ctx.widgets.component(STORE_WIDGET_ID, STORE_BOUNDARIES_COMPONENT_ID, STORE_CLOSE_COMPONENT_ID);
		if (!component.valid() || !component.visible()) {
			return false;
		}

		if (component.interact(ACTION_CLOSE)) {
			return Condition.wait(() -> !opened());
		}
		return false;
	}

	/**
	 *  Gets the list of items in the store
	 * @return List of items in store
	 *         Returns null if store is not open.
	 */
	public Component[] items() {
		if (!opened()) {
			return null;
		}
		Component components = ctx.widgets.component(STORE_WIDGET_ID, STORE_ITEMS_COMPONENT_ID);
		if (!components.valid() || !components.visible()) {
			return null;
		}

		return Arrays.stream(components.components()).filter(x -> x.itemId() != -1).toArray(Component[]::new);
	}

	/**
	 *  Attempts to buy items from the store using increments of 1,5,10,50 corresponding to the buy action
	 * @param itemId Id of item you wish to buy
	 * @param buyAction The increment you wish to buy in
	 * @return True if successfully bought and appears in your inventory
	 */
	public boolean buy(int itemId, int buyAction) {
		Component itemComponent = getItem(itemId);
		if (itemComponent == null) {
			return false;
		}

		long previousCount = ctx.inventory.toStream().id(itemId).mapToInt(Item::stackSize).sum();

		if (itemComponent.interact("Buy " + buyAction)) {
			return Condition.wait(() -> previousCount < ctx.inventory.toStream().id(itemId).mapToInt(Item::stackSize).sum());
		}

		return false;
	}

	/**
	 *  Gets the component of a specific item in the store
	 * @param itemId Id of the item you wish to find
	 * @return Component if found.
	 */
	public Component getItem(int itemId) {
		if (!opened()) {
			return Component.NIL;
		}

		Component components = ctx.widgets.component(STORE_WIDGET_ID, STORE_ITEMS_COMPONENT_ID);
		if (!components.valid() || !components.visible()) {
			return Component.NIL;
		}

		Component itemComponent = Arrays.stream(components.components()).filter(x -> x.itemId() == itemId).findFirst().orElse(null);
		if (itemComponent == null || !itemComponent.valid() || !itemComponent.visible()) {
			return Component.NIL;
		}
		return itemComponent;
	}

	/**
	 *  Returns the name of the store currently open
	 * @return The name of a store currently open, null if not able to find.
	 */
	public String shopName() {
		if (!opened()) {
			return null;
		}
		Component component = ctx.widgets.component(STORE_WIDGET_ID, STORE_BOUNDARIES_COMPONENT_ID, STORE_SHOPNAME_SUB_COMPONENT_ID);
		return component.text();
	}

	private Widget getStoreWidget() {
		return ctx.widgets.widget(STORE_WIDGET_ID);
	}

	/**
	 *  Gets the buy quantity current selected in the store.
	 */
	public int getBuyQuantity() {
		int buyQuantity = ctx.varpbits.varpbit(1022);
		switch (buyQuantity) {
			case 0:
				return 0;// Price Check
			case 1:
				return 1;
			case 2:
				return 5;
			case 3:
				return 10;
			case 4:
				return 50;
		}
		return -1;
	}

	/**
	 *  Sets the buy quantity in the store
	 * @param quantity Integer value of quantity 1,5,10,50
	 * @return True if set correctly.
	 */
	public boolean setBuyQuantity(int quantity) {
		if (!opened()) {
			return false;
		}

		if (getBuyQuantity() == quantity) {
			return true;
		}

		int widgetId = getQuantityWidgetId(quantity);
		if (widgetId == -1) {
			return false;
		}

		Component component = ctx.widgets.component(STORE_WIDGET_ID, widgetId);

		if (!component.valid() || !component.visible()) {
			return false;
		}
		if (component.interact("Buy-" + quantity)) {
			return Condition.wait(() -> getBuyQuantity() == quantity);
		}

		return false;
	}

	private int getQuantityWidgetId(int quantity) {
		switch (quantity) {
			case 0:
				return STORE_QUANTITY_VALUE_COMPONENT_ID;// Price Check
			case 1:
				return STORE_QUANTITY_ONE_COMPONENT_ID;
			case 5:
				return STORE_QUANTITY_FIVE_COMPONENT_ID;
			case 10:
				return STORE_QUANTITY_TEN_COMPONENT_ID;
			case 50:
				return STORE_QUANTITY_FIFTY_COMPONENT_ID;
			default:
				return -1;
		}
	}
}
