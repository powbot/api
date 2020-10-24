package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.HashTable;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.bot.rt4.client.WidgetNode;
import org.powerbot.script.Calculations;

import java.awt.*;

/**
 * Component
 * An object representing a graphical component of the Runescape user interfcace.
 */
public class Component extends Interactive {
	public static final Color TARGET_STROKE_COLOR = new Color(0, 255, 0, 150);
	public static final Color TARGET_FILL_COLOR = new Color(0, 0, 0, 50);

	private final Widget widget;
	private final Component component;
	private final int index;

	Component(final ClientContext ctx, final Widget widget, final int index) {
		this(ctx, widget, null, index);
	}

	Component(final ClientContext ctx, final Widget widget, final Component component, final int index) {
		super(ctx);
		this.widget = widget;
		this.component = component;
		this.index = index;
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

	public Component parent() {
		if (component == null) {
			return new Component(ctx, ctx.widgets.nil(), -1);
		}
		return component;
	}

	public int index() {
		return index;
	}

	private Component realParent() {
		if (valid()) {
			final int id = getInternal().getId();
			final int pid = getInternal().getParentId();
			final Component direct = ctx.widgets.widget(pid >> 16).component(pid & 0xff);
			if (direct != null && direct.valid()) {
				return direct;
			}

			for (final WidgetNode node : new HashTable<>(ctx.client().getWidgetTable(), WidgetNode.class)) {
				if (node != null && node.getUid() == id >> 16) {
					return ctx.widgets.widget(node.getUid() >> 16).component(node.getUid() & 0xff);
				}
			}
		}
		return new Component(ctx, ctx.widgets.nil(), -1);
	}

	public Point screenPoint() {
		return new Point(absX(), absY());
	}

	public int absX() {
		final Component parent = realParent();
		int absX = relativeX();
		if (parent != null && parent.valid()) {
			absX = absX + parent.absX();
		}

		if (type() > 0 && boundsIndex() > 0) {
			absX += ctx.client().getWidgetBoundsX()[boundsIndex()];
		}

		return absX;
	}


	public int absY() {
		final Component parent = realParent();
		int absY = relativeY();
		if (parent != null && parent.valid()) {
			absY += parent.absY();
		}

		if (type() > 0 && boundsIndex() > 0) {
			absY += ctx.client().getWidgetBoundsY()[boundsIndex()];
		}

		return absY;
	}

	public int relativeX() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getX() : -1;
	}

	public int relativeY() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getY() : -1;
	}

	public int width() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getWidth() : -1;
	}

	public int height() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getHeight() : -1;
	}

	public Rectangle boundingRect() {
		final Point p = screenPoint();
		return new Rectangle(p.x, p.y, width(), height());
	}

	public int borderThickness() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getBorderThickness() : -1;
	}

	public int type() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getType() : -1;
	}

	public int id() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getId() : -1;
	}

	public int parentId() {
		final Client client = ctx.client();
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		if (client == null || w == null) {
			return -1;
		}
		final int p = w.getParentId();
		if (p != -1) {
			return p;
		}

		final int uid = id() >>> 16;
		for (final WidgetNode node : new HashTable<>(client.getWidgetTable(), WidgetNode.class)) {
			if (node != null && uid == node.getUid()) {
				return (int) node.getNodeId();
			}
		}
		return -1;
	}

	public synchronized Component component(final int index) {
		if (index < 0) {
			return new Component(ctx, widget, this, -1);
		}

		return new Component(ctx, widget, this, index);
	}

	public synchronized int componentCount() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		final org.powerbot.bot.rt4.client.Widget[] arr = w != null ? w.getChildren() : null;
		return arr != null ? arr.length : 0;
	}

	public Component[] components() {
		final int len = componentCount();
		if (len <= 0) {
			return new Component[0];
		}
		final Component[] comps = new Component[len];
		for (int i = 0; i < len; i++) {
			comps[i] = component(i);
		}
		return comps;
	}

	public int contentType() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getContentType() : -1;
	}

	public int modelId() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getModelId() : -1;
	}

	public int modelType() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getModelType() : -1;
	}

	public int modelZoom() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getModelZoom() : -1;
	}

	public String[] actions() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		final String[] arr_ = (w != null ? w.getActions() : new String[0]);
		final String[] arr = arr_ != null ? arr_.clone() : new String[0];
		for (int i = 0; i < (arr != null ? arr.length : 0); i++) {
			if (arr[i] == null) {
				arr[i] = "";
			}
		}
		return arr != null ? arr : new String[0];
	}

	public int angleX() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getAngleX() : -1;
	}

	public int angleY() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getAngleY() : -1;
	}

	public int angleZ() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getAngleZ() : -1;
	}

	public String text() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		final String str = w != null ? w.getText() : "";
		return str != null ? str : "";
	}

	public String tooltip() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		final String str = w != null ? w.getTooltip() : "";
		return str != null ? str : "";
	}

	public int textColor() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getTextColor() : -1;
	}

	public int scrollX() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getScrollX() : -1;
	}

	public int scrollY() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getScrollY() : -1;
	}

	public int scrollWidth() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getScrollWidth() : -1;
	}

	public int scrollHeight() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getScrollHeight() : -1;
	}

	public int boundsIndex() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getBoundsIndex() : -1;
	}

	public int textureId() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getTextureId() : -1;
	}

	public int[] itemIds() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		final int[] a = w != null ? w.getItemIds() : new int[0];
		final int[] a2 = (a != null ? a : new int[0]).clone();
		for (int i = 0; i < a2.length; i++) {
			a2[i]--;
		}
		return a2;
	}

	public int[] itemStackSizes() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		final int[] a = w != null ? w.getItemStackSizes() : new int[0];
		return (a != null ? a : new int[0]).clone();
	}

	public int itemId() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getItemId() : -1;
	}

	public int itemStackSize() {
		final org.powerbot.bot.rt4.client.Widget w = getInternal();
		return w != null ? w.getItemStackSize() : -1;
	}

	@Override
	public Point basePoint() {
		return screenPoint();
	}

	@Override
	public Point centerPoint() {
		final Point p = screenPoint();
		p.translate(width() / 2, height() / 2);
		return p;
	}

	@Override
	public Point nextPoint() {
		final Rectangle interact = new Rectangle(screenPoint(), new Dimension(width(), height()));
		final int x = interact.x, y = interact.y;
		final int w = interact.width, h = interact.height;
		if (interact.x != -1 && interact.y != -1 && interact.width != -1 && interact.height != -1) {
			return Calculations.nextPoint(interact, new Rectangle(x + w / 2, y + h / 2, w / 4, h / 4));
		}
		return new Point(-1, -1);
	}

	@Override
	public boolean contains(final Point point) {
		final Point p = screenPoint();
		final Rectangle r = new Rectangle(p.x, p.y, width(), height());
		return r.contains(point);
	}

	@Override
	public boolean valid() {
		final org.powerbot.bot.rt4.client.Widget internal = getInternal();
		return internal != null && (component == null || component.visible()) &&
			id() != -1 && internal.getBoundsIndex() != -1;
	}

	public boolean visible() {
		final org.powerbot.bot.rt4.client.Widget internal = getInternal();
		int id = 0;
		if (internal != null && valid() && !internal.isHidden()) {
			id = parentId();
		}
		return id == -1 || (id != 0 && ctx.widgets.widget(id >> 16).component(id & 0xffff).visible());
	}

	private org.powerbot.bot.rt4.client.Widget getInternal() {
		final int wi = widget.id();
		if (wi < 1 || index < 0) {
			return null;
		}
		if (component != null) {
			final org.powerbot.bot.rt4.client.Widget _i = component.getInternal();
			final org.powerbot.bot.rt4.client.Widget[] arr = _i != null ? _i.getChildren() : null;
			if (arr != null && index < arr.length) {
				return arr[index];
			}
			return null;
		}
		final Client client = ctx.client();
		final org.powerbot.bot.rt4.client.Widget[][] arr = client != null ? client.getWidgets() : null;
		if (arr != null && wi < arr.length) {
			final org.powerbot.bot.rt4.client.Widget[] comps = arr[wi];
			return comps != null && index < comps.length ? comps[index] : null;
		}
		return null;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)[%s]", widget, component, index);
	}
}
