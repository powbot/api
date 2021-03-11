package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.HashTable;
import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.extended.IMobileClient;
import org.powerbot.bot.rt4.client.internal.IWidget;
import org.powerbot.bot.rt4.client.internal.IWidgetNode;
import org.powerbot.script.Calculations;
import org.powerbot.script.Identifiable;
import org.powerbot.script.Nillable;
import org.powerbot.script.Textable;

import java.awt.*;
import java.util.concurrent.Callable;

/**
 * Component
 * An object representing a graphical component of the Runescape user interfcace.
 */
@Deprecated
public class Component extends Interactive implements Textable, Identifiable, Nillable<Component> {
	public static final Component NIL = new Component(org.powerbot.script.ClientContext.ctx(), Widget.NIL, -1);

	public static final Color TARGET_STROKE_COLOR = new Color(0, 255, 0, 150);
	public static final Color TARGET_FILL_COLOR = new Color(0, 0, 0, 50);

	private final IWidget internal;
	private final Widget widget;
	private final Component component;
	private final int index;

	public Component(final ClientContext ctx, final Widget widget, final int index) {
		this(ctx, widget, null, index, null);
	}

	public Component(final ClientContext ctx, final Widget widget, final Component parentComponent, final int index, final IWidget internal) {
		super(ctx);
		this.widget = widget;
		this.component = parentComponent;
		this.index = index;
		this.internal = internal;
		setInteractive(getInternal());
	}

	@Override
	public void bounds(final int x1, final int x2, final int y1, final int y2, final int z1, final int z2) {
	}

	public CacheComponentConfig cacheConfig() {
		return widget.cacheConfigs()[index];
	}

	public Widget widget() {
		return widget;
	}

	@Deprecated
	public Component parent() {
		if (component == null) {
			return new Component(ctx, ctx.widgets.nil(), -1);
		}
		return component;
	}

	public int index() {
		return index;
	}

	public Point screenPoint() {
		final IWidget widget = getInternal();
		if (widget == null) {
			return new Point(-1, -1);
		}

		return widget.screenPoint();
	}

	public int relativeX() {
		final IWidget w = getInternal();
		return w != null ? w.getX() : -1;
	}

	public int relativeY() {
		final IWidget w = getInternal();
		return w != null ? w.getY() : -1;
	}

	public int width() {
		final IWidget w = getInternal();
		return w != null ? w.getWidth() : -1;
	}

	public int height() {
		final IWidget w = getInternal();
		return w != null ? w.getHeight() : -1;
	}

	public Rectangle boundingRect() {
		final Point p = screenPoint();
		return new Rectangle(p.x, p.y, width(), height());
	}

	public int borderThickness() {
		final IWidget w = getInternal();
		return w != null ? w.getBorderThickness() : -1;
	}

	public int type() {
		final IWidget w = getInternal();
		return w != null ? w.getType() : -1;
	}

	public int id() {
		final IWidget w = getInternal();
		return w != null ? w.getId() : -1;
	}

	public int parentId() {
		final IWidget w = getInternal();
		if (w == null) {
			return -1;
		}

		return w.parentId();
	}

	@Deprecated
	public synchronized Component component(final int index) {
		final IWidget w = getInternal();
		return w != null ? w.component(index) : Component.NIL;
	}

	public synchronized int componentCount() {
		final IWidget w = getInternal();
		return w != null ? w.componentCount() : -1;
	}

	public Component[] components() {
		final IWidget w = getInternal();
		return w != null ? w.components() : new Component[0];
	}

	public int contentType() {
		final IWidget w = getInternal();
		return w != null ? w.getContentType() : -1;
	}

	public int modelId() {
		final IWidget w = getInternal();
		return w != null ? w.getModelId() : -1;
	}

	public int modelType() {
		final IWidget w = getInternal();
		return w != null ? w.getModelType() : -1;
	}

	public int modelZoom() {
		final IWidget w = getInternal();
		return w != null ? w.getModelZoom() : -1;
	}

	public String[] actions() {
		final IWidget w = getInternal();
		return w != null ? w.actions() : new String[0];
	}

	public int angleX() {
		final IWidget w = getInternal();
		return w != null ? w.getAngleX() : -1;
	}

	public int angleY() {
		final IWidget w = getInternal();
		return w != null ? w.getAngleY() : -1;
	}

	public int angleZ() {
		final IWidget w = getInternal();
		return w != null ? w.getAngleZ() : -1;
	}

	public String text() {
		final IWidget w = getInternal();
		return w != null ? w.text() : "";
	}

	public String tooltip() {
		final IWidget w = getInternal();
		return w != null ?  w.tooltip() : "";
	}

	public int textColor() {
		final IWidget w = getInternal();
		return w != null ? w.getTextColor() : -1;
	}

	public int scrollX() {
		final IWidget w = getInternal();
		return w != null ? w.getScrollX() : -1;
	}

	public int scrollY() {
		final IWidget w = getInternal();
		return w != null ? w.getScrollY() : -1;
	}

	public int scrollWidth() {
		final IWidget w = getInternal();
		return w != null ? w.getScrollWidth() : -1;
	}

	public int scrollHeight() {
		final IWidget w = getInternal();
		return w != null ? w.getScrollHeight() : -1;
	}

	public int boundsIndex() {
		final IWidget w = getInternal();
		return w != null ? w.getBoundsIndex() : -1;
	}

	public int textureId() {
		final IWidget w = getInternal();
		return w != null ? w.getTextureId() : -1;
	}

	public int[] itemIds() {
		final IWidget w = getInternal();
		return w != null ? w.itemIds() : new int[0];
	}

	public int[] itemStackSizes() {
		final IWidget w = getInternal();
		return w != null ? w.itemStackSizes() : new int[0];
	}

	public int itemId() {
		final IWidget w = getInternal();
		return w != null ? w.getItemId() : -1;
	}

	public int itemStackSize() {
		final IWidget w = getInternal();
		return w != null ? w.getItemStackSize() : -1;
	}

	@Override
	public Point basePoint() {
		final IWidget w = getInternal();
		return w != null ? w.basePoint() : NIL_POINT;
	}

	@Override
	public Point centerPoint() {
		final IWidget w = getInternal();
		return w != null ? w.centerPoint() : NIL_POINT;
	}

	@Override
	public Point nextPoint() {
		final IWidget w = getInternal();
		return w != null ? w.nextPoint() : NIL_POINT;
	}

	@Override
	public boolean contains(final Point point) {
		final IWidget w = getInternal();
		return w != null && w.contains(point);
	}

	@Override
	public boolean valid() {
		final IWidget internal = getInternal();
		return internal != null && internal.valid();
	}

	public boolean visible() {
		final IWidget internal = getInternal();
		return internal != null && internal.visible();
	}

	private IWidget getInternal() {
		if (internal != null) {
			return internal;
		}

		final int wi = widget.id();
		if (wi < 1 || index < 0) {
			return null;
		}
		if (component != null) {
			final IWidget _i = component.getInternal();
			final IWidget[] arr = _i != null ? _i.getChildren() : null;
			if (arr != null && index < arr.length) {
				return arr[index];
			}
			return null;
		}
		final IClient client = ctx.client();
		final IWidget[][] arr = client != null ? client.getWidgets() : null;
		if (arr != null && wi < arr.length) {
			final IWidget[] comps = arr[wi];
			return comps != null && index < comps.length ? comps[index] : null;
		}
		return null;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)[%s]", widget, component, index);
	}

	@Override
	public Callable<Point> calculateScreenPosition() {
		final IWidget internal = getInternal();
		if (internal != null) {
			return internal.calculateScreenPosition();
		}

		return new Callable<Point>() {
			@Override
			public Point call() throws Exception {
				return NIL_POINT;
			}
		};
	}

	@Override
	public Component nil() {
		return NIL;
	}

}
