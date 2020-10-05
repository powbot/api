package org.powerbot.script.rt4;

import org.powerbot.script.*;

import java.awt.*;

/**
 * Item
 */
public class Item extends GenericItem implements Identifiable, Nameable, Stackable, Actionable {
	private static final int WIDTH = 42, HEIGHT = 36;
	final Component component;
	private final int inventoryIndex, id;
	private int stack;
	private int[] bounds;

	public Item(final ClientContext ctx, final Component component) {
		this(ctx, component, component.itemId(), component.itemStackSize());
	}

	public Item(final ClientContext ctx, final Component component, final int id, final int stack) {
		this(ctx, component, -1, id, stack);
	}

	public Item(final ClientContext ctx, final Component component, final int inventoryIndex, final int id, final int stack) {
		super(ctx);
		this.component = component;
		this.inventoryIndex = inventoryIndex;
		this.id = id;
		this.stack = stack;
	}

	/**
	 * Sets the bound of the item from the center. Do note that z1 and z2 are ignored.
	 */
	@Override
	public void bounds(final int x1, final int x2, final int y1, final int y2, final int z1, final int z2) {
		bounds = new int[]{x1, x2, y1, y2 };
	}

	@Override
	public int id() {
		return id;
	}

	@Override
	public Point centerPoint() {
		if (component == null) {
			return new Point(-1, -1);
		}
		if (inventoryIndex != -1) {
			final Point base = component.screenPoint();
			final int x = base.x - 3 + (inventoryIndex % 4) * WIDTH, y = base.y - 2 + (inventoryIndex / 4) * HEIGHT;
			return new Point(x + WIDTH / 2, y + HEIGHT / 2);
		}
		return component.centerPoint();
	}

	@Override
	public String name() {
		return StringUtils.stripHtml(super.name());
	}

	@Override
	public int stackSize() {
		if (component == null || !component.valid()) {
			return stack;
		}
		if (inventoryIndex != -1) {
			final int[] itemIds = component.itemIds();
			final int[] stackSizes = component.itemStackSizes();
			return (itemIds.length > inventoryIndex && stackSizes.length > inventoryIndex && itemIds[inventoryIndex] == id
					? stack = stackSizes[inventoryIndex]
					: stack);
		}
		if (component.visible() && component.itemId() == id) {
			return stack = component.itemStackSize();
		}
		return stack;
	}

	@Override
	public String[] actions() {
		return inventoryActions();
	}

	@Override
	public Point nextPoint() {
		if (component == null) {
			return new Point(-1, -1);
		}
		if (inventoryIndex != -1) {
			final Rectangle r = boundingRect();
			final int xOff = r.width / 8, yOff = r.height / 8;
			return Calculations.nextPoint(r, new Rectangle(r.x + r.width / 2 - xOff, r.y + r.height / 2 - yOff, r.width / 4, r.height / 4));
		}
		return component.nextPoint();
	}

	@Override
	public boolean contains(final Point point) {
		if (component == null) {
			return false;
		}
		if (inventoryIndex != -1) {
			final Rectangle r = boundingRect();
			return r.contains(point);
		}
		return component.contains(point);
	}

	public Component component() {
		return component;
	}

	@Override
	public boolean valid() {
		if (id == -1 || component == null || !component.valid()) {
			return false;
		}
		if (inventoryIndex != -1) {
			final int[] itemIds = component.itemIds();
			return itemIds.length > inventoryIndex && itemIds[inventoryIndex] == id;
		}
		return !component.visible() || component.itemId() == id;
	}

	public Rectangle boundingRect() {
		if (inventoryIndex == -1) return new Rectangle();
		Point base = component.screenPoint();
		final int x1, x2, y1, y2;
		if (bounds != null) {
			x1 = bounds[0];
			x2 = bounds[1];
			y1 = bounds[2];
			y2 = bounds[3];
			base = centerPoint();
			return new Rectangle(base.x + x1, base.y + y1, x2, y2);
		}
		return new Rectangle(base.x - 3 + (inventoryIndex % 4) * WIDTH, base.y - 2 + (inventoryIndex / 4) * HEIGHT, WIDTH, HEIGHT);
	}

	public int inventoryIndex() {
		return inventoryIndex;
	}
}
