package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.internal.IWidget;
import org.powerbot.script.*;

import java.util.Iterator;

/**
 * Widget
 */
public class Widget extends ClientAccessor implements Identifiable, Validatable,
	Iterable<Component>, Nillable<Widget> {
	private final int index;
	private CacheComponentConfig[] cacheConfigs;

	public static final Widget NIL = new Widget(org.powerbot.script.ClientContext.ctx(), -1);

	/**
	 * Represents an interactive display window which stores {@link Component}s
	 * and miscellaneous data.
	 *
	 * @param ctx   The {@link ClientContext}
	 * @param index The Widget index
	 */
	public Widget(final ClientContext ctx, final int index) {
		super(ctx);
		this.index = index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int id() {
		return index;
	}

	public CacheComponentConfig[] cacheConfigs() {
		if (cacheConfigs == null) {
			cacheConfigs = CacheComponentConfig.load(ctx.bot().getCacheWorker(), index);
		}
		return cacheConfigs;
	}

	/**
	 * Gets the component at the specified index.
	 *
	 * @param index The index of the component
	 * @return The component at the specified index, or <code>nil</code> if the
	 * component does not exist.
	 */
	public IWidget component(final int index) {
		final IClient client = ctx.client();
		if (index < 0 || client == null || client.getWidgets() == null || index >= client.getWidgets().length) {
			return Widgets.NIL;
		}

		final IWidget[] children = client.getWidgets()[index];
		if (children == null || children.length < index) {
			return Widgets.NIL;
		}

		return children[index];
	}

	public int componentCount() {
		final IClient client = ctx.client();
		final IWidget[][] arr = client != null ? client.getWidgets() : null;
		if (arr != null && index < arr.length) {
			final IWidget[] comps = arr[index];
			return comps != null ? comps.length : 0;
		}
		return 0;
	}

	/**
	 * An array of the nested components within the widget.
	 *
	 * @return A {@link Component} array
	 */
	public IWidget[] components() {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean valid() {
		if (index < 1) {
			return false;
		}
		final IClient client = ctx.client();
		final IWidget[][] arr = client != null ? client.getWidgets() : null;
		return arr != null && index < arr.length && arr[index] != null && arr[index].length > 0;
	}

	@Override
	public Iterator<Component> iterator() {
		final int count = componentCount();
		return new Iterator<Component>() {
			private int nextId = 0;

			@Override
			public boolean hasNext() {
				return nextId < count;
			}

			@Override
			public Component next() {
				IWidget comp = component(nextId++);
				if (comp != null) {
					return new Component(ctx, Widget.this, null, nextId, comp);
				}
				return Component.NIL;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + index + "]";
	}

	@Override
	public int hashCode() {
		return index;
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof Widget)) {
			return false;
		}
		final Widget w = (Widget) o;
		return w.index == index;
	}

	@Override
	public Widget nil() {
		return NIL;
	}
}
