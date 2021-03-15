package org.powerbot.script.rt4;

import org.powerbot.script.*;
import org.powerbot.script.action.Emittable;
import org.powerbot.script.action.ItemAction;

import java.awt.*;
import java.util.concurrent.Callable;

/**
 * Item
 */
public class Item extends GenericItem implements Identifiable, Nameable, Stackable, Actionable, Nillable<Item>,
	Emittable<ItemAction> {
	public static final Item NIL = new Item(org.powerbot.script.ClientContext.ctx(), Component.NIL);

	public static final int WIDTH = 31;
	public static final int HEIGHT = 31;
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
		bounds = new int[]{x1, x2, y1, y2};
	}

	@Override
	public int id() {
		return id;
	}

	@Override
	public Point basePoint() {
		final Point base = component.screenPoint();
		final int column = inventoryIndex % 4;
		final int row = inventoryIndex / 4;
		final int x = base.x + column * 42;
		final int y = base.y + row * 36;
		return new Point(x, y);
	}

	@Override
	public Point centerPoint() {
		if (component == null) {
			return new Point(-1, -1);
		}
		if (inventoryIndex != -1) {
			final Point base = basePoint();
			return new Point(base.x + WIDTH / 2, base.y + HEIGHT / 2);
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
			return Calculations.nextPoint(r, new Rectangle(r.x + r.width / 3, r.y + r.height / 3, r.width / 3, r.height / 3));
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
		Point base = basePoint();
		final int x1, x2, y1, y2;
		if (bounds != null) {
			x1 = bounds[0];
			x2 = bounds[1];
			y1 = bounds[2];
			y2 = bounds[3];
			base = centerPoint();
			return new Rectangle(base.x + x1, base.y + y1, x2, y2);
		}

		return new Rectangle(base, new Dimension(WIDTH, HEIGHT));
	}

	public int inventoryIndex() {
		return inventoryIndex;
	}

	@Override
	public Callable<Point> calculateScreenPosition() {
		return new Callable<>() {
			private Point lastTarget;

			@Override
			public Point call() {
				if (lastTarget == null) {
					lastTarget = nextPoint();
				}
				return lastTarget;
			}
		};
	}

	@Override
	public Item nil() {
		return NIL;
	}

	@Override
	public ItemAction createAction(String action) {
		return createAction(action, false);
	}

	@Override
	public ItemAction createAction(String action, boolean async) {
		Point p = nextPoint();

		return new ItemAction().
			setItemId(id).
			setSlot(inventoryIndex).
			setWidgetId(component.id()).
			setInteraction(action).
			setAsync(async).
			setEntityName(name()).
			setMouseX(p.x).
			setMouseY(p.y);
	}
}
