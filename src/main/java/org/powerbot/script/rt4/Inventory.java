package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Inventory
 */
public class Inventory extends ItemQuery<Item> {
	public Inventory(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> get() {
		final List<Item> items = new ArrayList<>(Constants.INVENTORY_SIZE);
		final Component comp = component();
		if (comp.componentCount() > 0) {
			for (final Component c : comp.components()) {
				final int id = c.itemId();
				if (id <= -1 || id == 6512 || c.itemStackSize() <= 0) {
					continue;
				}
				items.add(new Item(ctx, c, id, c.itemStackSize()));
			}
			return items;
		}
		final int[] ids = comp.itemIds(), stacks = comp.itemStackSizes();
		for (int i = 0; i < Math.min(ids != null ? ids.length : -1, stacks != null ? stacks.length : -1); i++) {
			final int id = ids[i], stack = stacks[i];
			if (id <= 0) {
				continue;
			}
			items.add(new Item(ctx, comp, i, id, stack));
		}
		return items;
	}

	public Item[] items() {
		final Item[] items = new Item[Constants.INVENTORY_SIZE];
		final Component comp = component();
		if (comp.componentCount() > 0) {
			final Component[] comps = comp.components();
			final int len = comps.length;
			for (int i = 0; i < Constants.INVENTORY_SIZE; i++) {
				if (i >= len) {
					items[i] = nil();
					continue;
				}
				final Component c = comps[i];
				final int id = c.itemId();
				if (id <= -1 || id == 6512 || c.itemStackSize() <= 0) {
					items[i] = nil();
					continue;
				}
				items[i] = new Item(ctx, c, id, c.itemStackSize());
			}
			return items;
		}
		final int[] ids = comp.itemIds(), stacks = comp.itemStackSizes();
		for (int i = 0; i < Math.min(ids != null ? ids.length : -1, stacks != null ? stacks.length : -1); i++) {
			final int id = ids[i], stack = stacks[i];
			if (id >= 1) {
				items[i] = new Item(ctx, comp, i, id, stack);
			} else {
				items[i] = nil();
			}
		}
		return items;
	}


	/**
	 * Gives you the item at a certain index.
	 *
	 * @param index range is 0-27.
	 * @return the item at the index. If the item index is greater than INVENTORY_SIZE, returns a nil item.
	 */
	public Item itemAt(final int index) {
		final Component comp = component(), itemComponent;
		if (index > -1 && index < Constants.INVENTORY_SIZE) {
			final int[] ids, stackSizes;
			if (comp.componentCount() > index && (itemComponent = comp.component(index)).id() > -1 && itemComponent.id() != 6512 && comp.itemStackSize() > -1) {
				return new Item(ctx, itemComponent, itemComponent.id(), itemComponent.itemStackSize());
			} else if ((ids = comp.itemIds()) != null && ids.length > index && ids[index] > -1 && (stackSizes = comp.itemIds()) != null && stackSizes[index] > -1) {
				return new Item(ctx, comp, index, ids[index], stackSizes[index]);
			}
		}
		return nil();
	}

	/**
	 * Gives you an item at a certain position.
	 *
	 * @param row    the row position for the inventory item (0-7)
	 * @param column the column position for the inventory item. (0-3)
	 * @return the item at the index. If the item index is greater than INVENTORY_SIZE, returns a nil item.
	 */
	public Item itemAt(final int row, final int column) {
		return itemAt(inventoryIndex(row, column));
	}

	public int selectionType() {
		final Client client = ctx.client();
		return client != null ? client.getSelectionType() : 0;
	}

	public int selectedItemIndex() {
		final Client client = ctx.client();
		return client != null && selectionType() == 1 ? client.getSelectionIndex() : -1;
	}

	public Item selectedItem() {
		final int index = selectedItemIndex();
		return itemAt(index);
	}

	public Component component() {
		Component c;
		if (!ctx.client().isMobile()) {
			for (final int[] alt : Constants.INVENTORY_ALTERNATIVES) {
				if ((c = ctx.widgets.widget(alt[0]).component(alt[1])).valid() && c.visible()) {
					return c;
				}
			}
		}
		return ctx.widgets.widget(Constants.INVENTORY_WIDGET).component(Constants.INVENTORY_ITEMS);
	}

	/**
	 * Drops specified item via regular or shift dropping.
	 *
	 * @param i     The item to drop
	 * @param shift Shift dropping, if true the method will verify it is enabled and fall back to regular if not
	 * @return Success
	 */
	public boolean drop(final Item i, final boolean shift) {
		if (shift && shiftDroppingEnabled()) {
			return ctx.input.send("{VK_SHIFT down}") && i.click(true) && ctx.input.send("{VK_SHIFT up}");
		} else {
			return i.interact("Drop", i.name());
		}
	}

	/**
	 * Drops specified items, uses shift dropping if enabled
	 *
	 * @param items The items to drop in query form
	 * @return Success
	 */
	public boolean drop(final ItemQuery<Item> items) {
		if (shiftDroppingEnabled()) {
			ctx.input.send("{VK_SHIFT down}");
			for (final Item i : items) {
				i.click(true);
				Condition.sleep(Random.getDelay());
				if (ctx.controller.isStopping()) {
					break;
				}
			}
			ctx.input.send("{VK_SHIFT up}");
			return true;
		} else {
			for (final Item i : items) {
				i.interact("Drop", i.name());
				if (ctx.controller.isStopping()) {
					break;
				}
			}
			return true;
		}
	}

	public boolean enableShiftDropping() {
		return false;
	}

	/**
	 * Gives the row of a given index.
	 * Does not throw an exception if index is out of range.
	 *
	 * @param index ranges from 0 - (INVENTORY_SIZE-1)
	 * @return the column position for the index (0-4)
	 */
	public int inventoryColumn(final int index) {
		return index % 4;
	}

	/**
	 * Gives the row of a given index.
	 * Does not throw an exception if index is out of range.
	 *
	 * @param index ranges from 0 - (INVENTORY_SIZE-1)
	 * @return the row position for the index (0-7)
	 */
	public int inventoryRow(final int index) {
		return Math.floorDiv(index, 4);
	}

	/**
	 * Gives the index for a given row and column.
	 * Does not throw an exception if row/column are out of range.
	 *
	 * @param row    the row position for the inventory item (0-7)
	 * @param column the column position for the inventory item. (0-3)
	 * @return the index for the given.
	 */
	public int inventoryIndex(final int row, final int column) {
		return row * 4 + column;
	}

	/**
	 * Converts an index to it's positional representation.
	 * Example, item at index 0 is returned as (0,0) or 6 as (1,2).
	 * This exists only for people who want a single atomic call to get inventory column and row.
	 * Doesn't throw an excception if out of bound.
	 *
	 * @param index 0-(Constants.INVENTORY_SIZE-1), index of inventory
	 * @return position representation of the index
	 */
	public Point indexPosition(final int index) {
		return new Point(inventoryColumn(index), inventoryRow(index));
	}

	/**
	 * Gives center for a position (row, column).
	 * Example, item at index 0 is referenced by (0,0) or 6 by (1,2).
	 *
	 * @param row    the row position for the inventory item (0-7)
	 * @param column the column position for the inventory item. (0-3)
	 * @return centre point of the specified point
	 * @throws IndexOutOfBoundsException if x is not between 0 to 3 or y is not between 0 to 6
	 */
	public Point indexCenterPoint(final int row, final int column) {
		if (column < 0 || column > 3 || row < 0 || row > 6) {
			throw new IndexOutOfBoundsException();
		}
		//DIFFERENCE BETWEEN CENTER POINTS
		final int xFactor = (column * Constants.INVENTORY_ITEM_X_DIFFERENCE) + Constants.INVENTORY_ITEM_BASE_X_DIFFERENCE;
		final int yFactor = (row * Constants.INVENTORY_ITEM_Y_DIFFERENCE) + Constants.INVENTORY_ITEM_BASE_Y_DIFFERENCE;
		//INVENTORY WIDGET POSITION
		final Point inventoryBase = ctx.widgets.component(Constants.INVENTORY_WIDGET, 0).screenPoint();
		return new Point(inventoryBase.x + xFactor, inventoryBase.y + yFactor);
	}

	/**
	 * Finds the center point of the inventory's index
	 *
	 * @param index 0-(Constants.INVENTORY_SIZE-1), index of inventory
	 * @return indexCenterPoint of the index param
	 * @throws IndexOutOfBoundsException if index is below 0 or above (Constants.INVENTORY_SIZE-1)
	 */
	public Point indexCenterPoint(final int index) {
		if (index < 0 || index > Constants.INVENTORY_SIZE - 1) {
			throw new IndexOutOfBoundsException();
		}
		return indexCenterPoint(inventoryRow(index), inventoryColumn(index));
	}

	/**
	 * Finds the boundingRectangle of the desired index, not every area within the rectangle will click the item
	 *
	 * @param index 0-(Constants.INVENTORY_SIZE-1), index of inventory
	 * @return boundingRectangle of the index param
	 * @throws IndexOutOfBoundsException if index is below 0 or above (Constants.INVENTORY_SIZE-1)
	 */
	public Rectangle boundingRect(final int index) {
		final int xFactor = Constants.INVENTORY_ITEM_WIDTH / 2, yFactor = Constants.INVENTORY_ITEM_HEIGHT / 2;
		final Point centerPoint = indexCenterPoint(index);
		return new Rectangle(centerPoint.x - xFactor, centerPoint.y - yFactor, Constants.INVENTORY_ITEM_WIDTH, Constants.INVENTORY_ITEM_HEIGHT);
	}

	/**
	 * Drags the given item to the given index
	 *
	 * @param item  Item to be dragged
	 * @param index Index to drag the item to
	 * @return True if the item is at the index or the drag was successful, false otherwise
	 * @throws IndexOutOfBoundsException if index is below 0 or above (Constants.INVENTORY_SIZE-1)
	 */
	public boolean drag(final Item item, final int index) {
		if (!item.valid()) {
			return false;
		}

		if (item.inventoryIndex() == index) {
			return true;
		}

		if (index < 0 || index > Constants.INVENTORY_SIZE - 1) {
			throw new IndexOutOfBoundsException();
		}

		if (!ctx.input.move(item.nextPoint())) {
			return false;
		}

		final Rectangle r = boundingRect(index);
		final int xOff = r.width / 8, yOff = r.height / 8;
		final Rectangle objectRectangle = new Rectangle(r.x + r.width / 2 - xOff, r.y + r.height / 2 - yOff, r.width / 4, r.height / 4);

		return ctx.input.drag(Calculations.nextPoint(r, objectRectangle), true);
	}

	public boolean shiftDroppingEnabled() {
		return ctx.varpbits.varpbit(1055, 17, 0x1) == 1;
	}

	public boolean isFull() {
		return ctx.inventory.select().size() >= Constants.INVENTORY_SIZE;
	}

	@Override
	public Item nil() {
		return new Item(ctx, null, -1, -1, -1);
	}

}
