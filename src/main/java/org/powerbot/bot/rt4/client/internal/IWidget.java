package org.powerbot.bot.rt4.client.internal;

import org.powerbot.bot.rt4.HashTable;
import org.powerbot.bot.rt4.client.extended.IMobileClient;
import org.powerbot.script.*;
import org.powerbot.script.ClientContext;
import org.powerbot.script.Interactive;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.Component;

import java.awt.*;
import java.util.concurrent.Callable;

public interface IWidget extends INode, Interactive, Textable, Identifiable, Nillable<IWidget> {

	String[] getActions();

	int getAngleX();

	int getAngleY();

	int getAngleZ();

	int getBorderThickness();

	int getBoundsIndex();

	IWidget[] getChildren();

	int getContentType();

	int getHeight();

	int getId();

	int getItemId();

	int[] getItemIds();

	int getItemStackSize();

	int[] getItemStackSizes();

	int getModelId();

	int getModelType();

	int getModelZoom();

	int getParentId();

	int getScrollHeight();

	int getScrollWidth();

	int getScrollX();

	int getScrollY();

	String getText();

	int getTextColor();

	int getTextureId();

	String getTooltip();

	int getType();

	int getWidth();

	int getX();

	int getY();

	boolean isHidden();

	@Override
	default void draw(Graphics render) {
		Rectangle bounds = boundingRect();
		if (bounds != null) {
			render.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
		}
	}

	@Override
	default void draw(Graphics render, int alpha) {
		draw(render);
	}

	@Override
	default Point basePoint() {
		return screenPoint();
	}

	@Override
	default Point centerPoint() {
		final Point p = screenPoint();
		p.translate(getWidth() / 2, getHeight() / 2);
		return p;
	}

	@Override
	default Callable<Point> calculateScreenPosition() {
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
	default Point nextPoint() {
		final Rectangle interact = new Rectangle(screenPoint(), new Dimension(getWidth(), getHeight()));
		final int x = interact.x, y = interact.y;
		final int w = interact.width, h = interact.height;
		if (interact.x != -1 && interact.y != -1 && interact.width != -1 && interact.height != -1) {
			interact.x += 1;
			interact.y += 1;
			interact.width -= 1;
			interact.height -= 1;
			return Calculations.nextPoint(interact, new Rectangle(x + w / 3, y + h / 3, w / 3, h / 3));
		}
		return new Point(-1, -1);
	}

	@Override
	default boolean contains(final Point point) {
		final Point p = screenPoint();
		final Rectangle r = new Rectangle(p.x, p.y, getWidth(), getHeight());
		return r.contains(point);
	}

	@Override
	default boolean valid() {
		return visible() && getId() != -1 && getBoundsIndex() != -1;
	}

	default boolean visible() {
		int id = 0;
		if (!isHidden()) {
			id = parentId();
		}
		return id == -1 || (id != 0 && org.powerbot.script.rt4.ClientContext.ctx().widgets.widget(id >> 16).component(id & 0xffff).visible());
	}

	@Override
	default boolean inViewport() {
		return valid();
	}

	default int parentId() {
		final int p = getParentId();
		if (p != -1) {
			return p;
		}
		final IClient client = ClientContext.ctx().client();

		if (client.isMobile()) {
			return ((IMobileClient) client).getWidgetParentId(getId());
		} else {
			final int uid = getId() >>> 16;
			HashTable<IWidgetNode> cache = new HashTable<>(client.getWidgetTable());
			for (IWidgetNode node = cache.getHead(); node != null; node = cache.getNext()) {
				if (uid == node.getUid()) {
					return (int) node.getNodeId();
				}
			}
			return -1;
		}
	}

	default Point screenPoint() {
		final IClient client = ClientContext.ctx().client();

		final int parentId = parentId();
		int x = getX(), y = getY();
		if (parentId != -1) {
			final IWidget parent = org.powerbot.script.rt4.ClientContext.ctx().widgets.component(parentId >> 16, parentId & 0xffff);
			if (parent != null) {
				final Point p = parent.screenPoint();
				x += p.x - parent.scrollX();
				y += p.y - parent.scrollY();
			}
		} else {
			final int index = getBoundsIndex();
			final int[] boundsX = client.getWidgetBoundsX();
			final int[] boundsY = client.getWidgetBoundsY();
			if (index >= 0 && boundsX.length > index && boundsX[index] >= 0 && boundsY.length > index && boundsY[index] >= 0) {
				return new Point(
					boundsX[index] + (getType() > 0 ? getX() : 0),
					boundsY[index] + (getType() > 0 ? getY() : 0)
				);
			}
		}
		return new Point(x, y);
	}

	default int scrollX() {
		return getScrollX();
	}

	default int scrollY() {
		return getScrollY();
	}

	default int height() {
		return getHeight();
	}

	default int width() {
		return getWidth();
	}

	default int textureId() {
		return getTextureId();
	}

	default int textColor() {
		return getTextColor();
	}

	default Rectangle boundingRect() {
		final Point p = screenPoint();
		return new Rectangle(p.x, p.y, getWidth(), getHeight());
	}

	@Override
	default int id() {
		return getId();
	}

	@Override
	default String text() {
		final String str =  getText();
		return str != null ? str : "";
	}

	default int[] itemIds() {
		final int[] a = getItemIds();
		final int[] a2 = (a != null ? a : new int[0]).clone();
		for (int i = 0; i < a2.length; i++) {
			a2[i]--;
		}
		return a2;
	}

	default int[] itemStackSizes() {
		final int[] a = getItemStackSizes();
		return (a != null ? a : new int[0]).clone();
	}

	default int itemId() {
		return getItemId();
	}

	default int itemStackSize() {
		return getItemStackSize();
	}


	default String tooltip() {
		final String str =  getTooltip();
		return str != null ? str : "";
	}

	default String[] actions() {
		final String[] arr_ = getActions();
		final String[] arr = arr_ != null ? arr_.clone() : new String[0];
		for (int i = 0; i < (arr != null ? arr.length : 0); i++) {
			if (arr[i] == null) {
				arr[i] = "";
			}
		}
		return arr != null ? arr : new String[0];
	}

	default IWidget getChild(final int index) {
		final IWidget[] children = getChildren();
		if (children == null || children.length < index) {
			return null;
		}

		return children[index];
	}

	default IWidget component(final int index) {
		if (index < 0) {
			return Widgets.NIL;
		}

		final IWidget[] children = getChildren();
		if (children == null || children.length < index) {
			return Widgets.NIL;
		}

		return children[index];
	}

	default int componentCount() {
		final IWidget[] arr = getChildren();
		return arr != null ? arr.length : 0;
	}

	@Deprecated
	default IWidget[] components() {
		final int len = componentCount();
		if (len <= 0) {
			return new IWidget[0];
		}
		final IWidget[] comps = new IWidget[len];
		for (int i = 0; i < len; i++) {
			comps[i] = component(i);
		}
		return comps;
	}

	@Override
	default IWidget nil() {
		return Widgets.NIL;
	}

	default CacheComponentConfig cacheConfig() {
		final CacheComponentConfig[] configs = new Widget(ClientContext.ctx(), parentId()).cacheConfigs();
		final int index = id() >> 16;
		if (configs == null || index >= configs.length) {
			return null;
		}

		return configs[index];
	}

	default int scrollHeight() {
		return getScrollHeight();
	}

	default int scrollWidth() {
		return getScrollWidth();
	}

	default int relativeX() {
		return getX();
	}

	default int relativeY() {
		return getY();
	}

	default int index() {
		return getId() >> 16;
	}

	default int borderThickness() {
		return getBorderThickness();
	}

	default int contentType() {
		return getContentType();
	}

	default int modelZoom() {
		return getModelZoom();
	}

	default Widget widget() {
		return new Widget(ClientContext.ctx(), parentId());
	}

	default int type() {
		return getType();
	}

	default int modelId() {
		return getModelId();
	}

	default int modelType() {
		return getModelType();
	}
}
