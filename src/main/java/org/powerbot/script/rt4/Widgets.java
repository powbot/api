package org.powerbot.script.rt4;

import org.powbot.stream.Streamable;
import org.powbot.stream.widget.WidgetStream;
import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.internal.INode;
import org.powerbot.bot.rt4.client.internal.IWidget;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Widgets
 */
public class Widgets extends IdQuery<Widget> implements Streamable<WidgetStream> {

	public Widgets(final ClientContext ctx) {
		super(ctx);
	}

	public Widget widget(final int index) {
		if (index < 0) {
			return new Widget(ctx, 0);
		}
		return new Widget(ctx, index);
	}

	/**
	 * Retrieves the cached {@link Component} of the given {@link Widget} index.
	 *
	 * @param index          the index of the desired {@link Widget}
	 * @param componentIndex the index of the desired {@link Component} of the given {@link Widget}
	 * @return the {@link Component} belonging to the {@link Widget} requested
	 */
	public Component component(final int index, final int componentIndex) {
		return widget(index).component(componentIndex);
	}

	public Component component(final int index, final int componentIndex, final int subComponentIndex) {
		return component(index, componentIndex).component(subComponentIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Widget> get() {
		final List<Widget> widgets = new ArrayList<>();
		final IClient client = ctx.client();
		final IWidget[][] a = client != null ? client.getWidgets() : null;
		final int len = a != null ? a.length : 0;
		if (len <= 0) {
			return new ArrayList<>(0);
		}
		for (int i = 0; i < a.length; i++) {
			widgets.add(widget(i));
		}
		return widgets;
	}

	/**
	 * Returns all the {@link Widget}s that are currently loaded in the game.
	 *
	 * @return an array of {@link Widget}s which are currently loaded
	 * @deprecated use queries
	 */
	@Deprecated
	public Widget[] array() {
		final List<Widget> w = get();
		return w.toArray(new Widget[0]);
	}

	/**
	 * Scrolls to view the provided component, if it's not already in view.
	 *
	 * @param pane      the viewport component
	 * @param component the viewport component
	 * @param bar       the scrollbar
	 * @return {@code true} if scrolled to view, otherwise {@code false}
	 * @deprecated use {@link #scroll(Component, Component, Component, boolean) scroll(component, pane, bar, scroll)}
	 */
	@Deprecated
	public boolean scroll(final Component pane, final Component component, final Component bar) {
		return scroll(component, pane, bar, true);
	}

	/**
	 * Scrolls to view the provided component, if it's not already in view.
	 *
	 * @param component   the component to scroll to
	 * @param pane        the viewport component
	 * @param bar         the scrollbar
	 * @param mouseScroll whether to use mouse wheel to scroll or not
	 * @return {@code true} if scrolled to view or is already in view, otherwise {@code false}
	 */
	public boolean scroll(final Component component, final Component pane, final Component bar, final boolean mouseScroll) {
		if (component == null || !component.valid()) {
			return false;
		}
		final int childrenCount;
		if (bar == null || !bar.valid() || ((childrenCount = bar.componentCount()) != 6 && childrenCount != 7)) {
			return false;
		}

		if (pane == null || !pane.valid()) {
			return false;
		}

		if (pane.scrollHeight() == 0) {
			return true;
		}

		final Point view = pane.screenPoint();
		final int height = pane.height();
		if (view.x < 0 || view.y < 0 || height < 1) {
			return false;
		}
		final Point pos = component.screenPoint();
		final int length = component.height();
		if (pos.y >= view.y && pos.y <= view.y + height - length) {
			return true;
		}
		final Component thumbHolder = bar.component(0);
		final Component thumb = bar.component(1);
		final int thumbSize = thumbHolder.height();
		int y = (int) ((float) thumbSize / pane.scrollHeight() *
			(component.relativeY() + Random.nextInt(-height / 2, height / 2 - length)));
		if (y < 0) {
			y = 0;
		} else if (y >= thumbSize) {
			y = thumbSize - 1;
		}
		final Point p = thumbHolder.screenPoint();
		p.translate(Random.nextInt(0, thumbHolder.width()), y);
		if (!mouseScroll) {
			if (!ctx.input.click(p, true)) {
				return false;
			}
			Condition.sleep();
		}
		Point a;
		Component c;
		int tY = thumb.screenPoint().y;
		final long start = System.nanoTime();
		long mark = System.nanoTime();
		int scrolls = 0;
		while ((a = component.screenPoint()).y < view.y || a.y > view.y + height - length) {
			if (System.nanoTime() - start >= TimeUnit.SECONDS.toNanos(20)) {
				break;
			}
			if (mouseScroll && (pane.contains(ctx.input.getLocation()) || pane.hover())) {
				if (ctx.input.scroll(a.y > view.y)) {
					if (++scrolls >= Random.nextInt(9, 13)) {
						Condition.sleep(Random.getDelay() * Random.nextInt(3, 9));
						scrolls = 0;
					}
					Condition.sleep(Random.getDelay() / Random.nextInt(1, 3));
					if (System.nanoTime() - mark > TimeUnit.SECONDS.toNanos(2)) {
						final int l = thumb.screenPoint().y;
						if (tY == l) {
							return scroll(component, pane, bar, false);
						} else {
							mark = System.nanoTime();
							tY = l;
						}
					}
				} else {
					break;
				}
			} else {
				c = bar.component(a.y < view.y ? 4 : 5);
				if (c == null) {
					break;
				}
				if (c.hover()) {
					ctx.input.press(MouseEvent.BUTTON1);
					if (!Condition.wait(new Condition.Check() {
						@Override
						public boolean poll() {
							final Point a = component.screenPoint();
							return a.y >= view.y && a.y <= view.y + height - length;
						}
					}, 500, 10)) {
						++scrolls;
					}
					ctx.input.release(MouseEvent.BUTTON1);
				}
				if (scrolls >= 3) {
					return false;
				}
			}
		}
		return a.y >= view.y && a.y <= height + view.y + height - length;
	}

	/**
	 * Finds the close button among the components of the provided interface widget, and closes it using mouse.
	 *
	 * @param interfaceWidget Widget of interface that is being closed
	 * @return {@code true} if the interface is not opened or was successfully closed, {@code false} otherwise.
	 **/
	public boolean close(final Widget interfaceWidget) {
		return close(interfaceWidget.components(), false);
	}

	/**
	 * Finds the close button among the components of the provided interface widget, and closes it using either mouse or hotkey.
	 * WARNING: It is recommended to use the overloaded method {@link #close(Widget) close(interfaceWidget)}. In the future, when the antipatterns are implemented, the client will automatically decide whether or not to use hotkeys to close the interface.
	 *
	 * @param interfaceWidget Widget of interface that is being closed
	 * @param hotkey          Whether or not use hotkey to close the interface
	 * @return {@code true} if the interface is not opened or was successfully closed, {@code false} otherwise.
	 **/
	public boolean close(final Widget interfaceWidget, final boolean hotkey) {
		return close(interfaceWidget.components(), hotkey);
	}

	/**
	 * Finds the close button among the provided interface components, and closes it using mouse.
	 *
	 * @param interfaceComponents Components of interface that is being closed
	 * @return {@code true} if the interface is not opened or was successfully closed, {@code false} otherwise.
	 **/
	public boolean close(final Component[] interfaceComponents) {
		return close(findCloseButton(interfaceComponents), false);
	}

	/**
	 * Finds the close button among the provided interface components, and closes it using either mouse or hotkey.
	 * WARNING: It is recommended to use the overloaded method {@link #close(Component[]) close(interfaceComponents)}. In the future, when the antipatterns are implemented, the client will automatically decide whether or not to use hotkeys to close the interface.
	 *
	 * @param interfaceComponents Components of interface that is being closed
	 * @param hotkey              Whether or not use hotkey to close the interface
	 * @return {@code true} if the interface is not opened or was successfully closed, {@code false} otherwise.
	 **/
	public boolean close(final Component[] interfaceComponents, final boolean hotkey) {
		return close(findCloseButton(interfaceComponents), hotkey);
	}

	/**
	 * Closes the parent interface of the closeButton component using mouse.
	 *
	 * @param closeButton The button which closes the interface
	 * @return {@code true} if the interface is not opened or was successfully closed, {@code false} otherwise.
	 **/
	public boolean close(final Component closeButton) {
		return close(closeButton, false);
	}

	/**
	 * Closes the parent interface of the closeButton component using either mouse or hotkey.
	 * WARNING: It is recommended to use the overloaded method {@link #close(Component) close(closeButton)}. In the future, when the antipatterns are implemented, the client will automatically decide whether or not to use hotkeys to close the interface.
	 *
	 * @param closeButton The button which closes the interface
	 * @param hotkey      Whether or not use hotkey to close the interface
	 * @return {@code true} if the interface is not opened or was successfully closed, {@code false} otherwise.
	 **/
	public boolean close(final Component closeButton, final boolean hotkey) {
		if (closeButton == null || !closeButton.valid()) {
			return true;
		}
		return (hotkey ? ctx.input.send("{VK_ESCAPE}") : closeButton.click()) && Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return !closeButton.valid();
			}
		}, 50, 16);
	}

	private Component findCloseButton(final Component[] components) {
		for (final Component c1 : components) {
			if (c1.componentCount() == 0) {
				final int t1 = c1.textureId();
				for (final int texture : Constants.CLOSE_BUTTON_TEXTURES) {
					if (t1 == texture) {
						return c1;
					}
				}
			} else {
				final Component c2 = findCloseButton(c1.components());
				if (c2 != null) {
					return c2;
				}
			}
		}
		return null;
	}

	@Override
	public Widget nil() {
		return Widget.NIL;
	}

	@Override
	public boolean isNil() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WidgetStream toStream() {
		return new WidgetStream(ctx, get().stream());
	}

	public static final IWidget NIL = new IWidget() {
		@Override
		public String[] getActions() {
			return new String[0];
		}

		@Override
		public int getAngleX() {
			return 0;
		}

		@Override
		public int getAngleY() {
			return 0;
		}

		@Override
		public int getAngleZ() {
			return 0;
		}

		@Override
		public int getBorderThickness() {
			return 0;
		}

		@Override
		public int getBoundsIndex() {
			return 0;
		}

		@Override
		public IWidget[] getChildren() {
			return new IWidget[0];
		}

		@Override
		public int getContentType() {
			return 0;
		}

		@Override
		public int getHeight() {
			return 0;
		}

		@Override
		public int getId() {
			return -1;
		}

		@Override
		public int getItemId() {
			return 0;
		}

		@Override
		public int[] getItemIds() {
			return new int[0];
		}

		@Override
		public int getItemStackSize() {
			return 0;
		}

		@Override
		public int[] getItemStackSizes() {
			return new int[0];
		}

		@Override
		public int getModelId() {
			return 0;
		}

		@Override
		public int getModelType() {
			return 0;
		}

		@Override
		public int getModelZoom() {
			return 0;
		}

		@Override
		public int getParentId() {
			return 0;
		}

		@Override
		public int getScrollHeight() {
			return 0;
		}

		@Override
		public int getScrollWidth() {
			return 0;
		}

		@Override
		public int getScrollX() {
			return 0;
		}

		@Override
		public int getScrollY() {
			return 0;
		}

		@Override
		public String getText() {
			return null;
		}

		@Override
		public int getTextColor() {
			return 0;
		}

		@Override
		public int getTextureId() {
			return 0;
		}

		@Override
		public String getTooltip() {
			return null;
		}

		@Override
		public int getType() {
			return 0;
		}

		@Override
		public int getWidth() {
			return 0;
		}

		@Override
		public int getX() {
			return 0;
		}

		@Override
		public int getY() {
			return 0;
		}

		@Override
		public boolean isHidden() {
			return false;
		}

		@Override
		public INode getNext() {
			return null;
		}

		@Override
		public long getNodeId() {
			return 0;
		}

		@Override
		public boolean isNil() {
			return true;
		}
	};
}
