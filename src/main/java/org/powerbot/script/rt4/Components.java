package org.powerbot.script.rt4;

import org.powbot.stream.widget.ComponentStream;
import org.powbot.stream.Streamable;
import org.powerbot.bot.rt4.client.internal.IWidget;
import org.powerbot.script.*;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.regex.Pattern;

public class Components extends AbstractQuery<Components, Component, ClientContext> implements Streamable<ComponentStream> {

	public static final IWidget NIL = Widgets.NIL;

	public Components(final ClientContext ctx) {
		super(ctx);
	}

	@Override
	protected Components getThis() {
		return this;
	}

	/**
	 * Loads components, along with their children, contained in the provided widgets.
	 *
	 * @param widgets widgets to load components for.
	 * @return {@code this} for the purpose of chaining.
	 */
	public Components select(final Widget... widgets) {
		return select(true, widgets);
	}

	/**
	 * Loads components contained in the provided widgets.
	 *
	 * @param children whether or not to load children components (nested components).
	 * @param widgets  widgets to load components for.
	 * @return {@code this} for the purpose of chaining.
	 */
	public Components select(final boolean children, final Widget... widgets) {
		return select(get(children, Arrays.asList(widgets)));
	}

	/**
	 * Loads components, along with their children, contained in the widgets loaded from the provided ids.
	 *
	 * @param widgetIds ids of widgets to load components for.
	 * @return {@code this} for the purpose of chaining.
	 */
	public Components select(final int... widgetIds) {
		return select(true, widgetIds);
	}

	/**
	 * Loads components contained in the widgets loaded from the provided ids.
	 *
	 * @param children  whether or not to load children components (nested components).
	 * @param widgetIds ids of widgets to load components for.
	 * @return {@code this} for the purpose of chaining.
	 */
	public Components select(final boolean children, final int... widgetIds) {
		final List<Widget> widgets = new ArrayList<>();
		for (final int id : widgetIds) {
			widgets.add(ctx.widgets.widget(id));
		}
		return select(get(children, widgets));
	}

	/**
	 * Loads sub-components contained in the parent component inside the widget.
	 *
	 * @param widget    id of the widget to load.
	 * @param component index of the component whose children to load.
	 * @return {@code this} for the purpose of chaining.
	 */
	public Components select(final int widget, final int component) {
		return select(Arrays.asList(wrap(null, ctx.widgets.component(widget, component).components())));
	}


	@Override
	public List<Component> get() {
		return get(true, ctx.widgets.select());
	}

	private List<IWidget> walkWidgets(final IWidget[] widgets) {
		final List<IWidget> all = new ArrayList<>();
		for (IWidget child : widgets) {
			all.add(child);
			if (child.getChildren() != null && child.getChildren().length > 0) {
				all.addAll(walkWidgets(child.getChildren()));
			}
		}
		return all;
	}

	private List<IWidget> getInternal() {
		final List<IWidget> all = new ArrayList<>();
		final IWidget[][] widgets = ctx.client().getWidgets();
		for (IWidget[] group : widgets) {
			if (group == null) {
				continue;
			}

			all.addAll(walkWidgets(group));
		}

		return all;
	}

	private List<Component> get(final boolean children, final Iterable<Widget> widgets) {
		final Queue<Component> base = new ArrayDeque<>();
		final List<Component> components = new ArrayList<>();
		for (final Widget w : widgets) {
			Collections.addAll(base, wrap(w, w.components()));
		}
		while (!base.isEmpty()) {
			final Component c = base.poll();
			if (children && c.components().length > 0) {
				Collections.addAll(base, wrap(null, c.components()));
			} else {
				components.add(c);
			}
		}
		return components;
	}

	private Component[] wrap(final Widget parent, final IWidget[] widgets) {
		final Component[] wrapped = new Component[widgets.length];
		for (int i = 0; i < widgets.length; i++) {
			wrapped[i] = new Component(ctx, parent, null, widgets[i].id() >> 16, widgets[i]);
		}

		return wrapped;
	}


	@Override
	public Component nil() {
		return Component.NIL;
	}

	@Override
	public boolean isNil() {
		return false;
	}

	/**
	 * Filters the current query's contents to only include components that are currently visible
	 *
	 * @return filtered query
	 */
	public Components visible() {
		return select(Component::visible);
	}

	/**
	 * Filters the current query's contents to only include components that are in the viewport
	 *
	 * @return filtered query
	 */
	public Components inViewport() {
		return select(Interactive::inViewport);
	}

	/**
	 * Filters for components which are containers of other components (parents of children components).
	 *
	 * @return {@code this} for the purpose of chaining.
	 */
	public Components parents() {
		return select(c -> c.componentCount() > 0);
	}

	/**
	 * Filters the current query's contents to only include components who match the specified widget index
	 *
	 * @param index index to find
	 * @return filtered query
	 */
	public Components widget(final int index) {
		return select(component -> component.widget().id() == index);
	}


	/**
	 * Filters the current query's contents to only include components whose text contains at least one (1) of the parameter strings.
	 * Comparisons are NOT case sensitive.
	 *
	 * @param text [varargs] the strings to compare the component text to
	 * @return filtered query
	 */
	public Components textContains(final String... text) {
		final String[] arr = new String[text.length];
		for (int i = 0; i < text.length; i++) {
			arr[i] = text[i].toLowerCase();
		}
		return select(component -> {
			final String text1 = component.text().toLowerCase().trim();
			for (final String s : arr) {
				if (text1.contains(s)) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Filters the current query's contents to only include components whose text equals to at least one (1) of the parameter strings.
	 * Comparisons ARE case sensitive.
	 *
	 * @param strings [varargs] the strings to compare the component text to
	 * @return filtered query
	 */
	public Components text(final String... strings) {
		return select(component -> {
			final String text = component.text().trim();
			for (final String s : strings) {
				if (s.equalsIgnoreCase(text)) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Filters the current query's contents to only include components whose text matches the parameter Pattern
	 *
	 * @param pattern the Pattern to compare component text to
	 * @return filtered query
	 */
	public Components text(final Pattern pattern) {
		return select(component -> pattern.matcher(component.text().trim()).find());
	}


	/**
	 * Filters the current query's contents to only include components whose tooltip contains at least one (1) of the parameter strings.
	 * Comparisons are NOT case sensitive.
	 *
	 * @param text [varargs] the strings to compare the component tooltip to
	 * @return filtered query
	 */
	public Components tooltipContains(final String... text) {
		final String[] arr = new String[text.length];
		for (int i = 0; i < text.length; i++) {
			arr[i] = text[i].toLowerCase();
		}
		return select(component -> {
			final String text1 = component.tooltip().toLowerCase().trim();
			for (final String s : arr) {
				if (text1.contains(s)) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Filters the current query's contents to only include components whose tooltip equals to at least one (1) of the parameter strings.
	 * Comparisons ARE case sensitive.
	 *
	 * @param strings [varargs] the strings to compare the component tooltip to
	 * @return filtered query
	 */
	public Components tooltip(final String... strings) {
		return select(component -> {
			final String text = component.tooltip().trim();
			for (final String s : strings) {
				if (s.equalsIgnoreCase(text)) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Filters the current query's contents to only include components whose tooltip matches the parameter Pattern
	 *
	 * @param pattern the Pattern to compare component tooltip to
	 * @return filtered query
	 */
	public Components tooltip(final Pattern pattern) {
		return select(component -> pattern.matcher(component.tooltip().trim()).find());
	}

	/**
	 * Filters the current query's contents to only include components whose content type matches at least one (1) of the parameter ints
	 *
	 * @param types the content types to compare component to
	 * @return filtered query
	 */
	public Components contentType(final int... types) {
		return select(component -> {
			for (final int type : types) {
				if (type == component.contentType()) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Filters the current query's contents to only include components whose model id matches at least one (1) of the parameter ints
	 *
	 * @param ids the model ids to compare component to
	 * @return filtered query
	 */
	public Components modelId(final int... ids) {
		return select(component -> {
			for (final int id : ids) {
				if (id == component.modelId()) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Filters the current query's contents to only include components whose item id matches at least one (1) of the parameter ints
	 *
	 * @param ids the item ids to compare component to
	 * @return filtered query
	 */
	public Components itemId(final int... ids) {
		return select(component -> {
			final int itemId = component.itemId();
			for (final int id : ids) {
				if (itemId == id) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Filters the current query's contents to only include components whose texture id matches at least one (1) of the parameter ints
	 *
	 * @param textures the texture ids to compare component to
	 * @return filtered query
	 */
	public Components texture(final int... textures) {
		return select(component -> {
			final int textureId = component.textureId();
			for (final int i : textures) {
				if (textureId == i) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Filters the current query's contents to only include components whose width and height bounds match at least one (1) of the parameter rectangles
	 *
	 * @param rectangles the rectangle bounds to compare component to
	 * @return filtered query
	 */
	public Components bounds(final Rectangle... rectangles) {
		return select(component -> {
			final int width = component.width();
			final int height = component.height();

			for (final Rectangle i : rectangles) {
				if (i.width == width && i.height == height) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Filters the current query's contents to only include components whose width matches at least one (1) of the parameter ints
	 *
	 * @param widths the widths to compare component to
	 * @return filtered query
	 */
	public Components width(final int... widths) {
		return select(c -> {
			final int width = c.width();
			for (final int w : widths) {
				if (width == w) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Filters the current query's contents to only include components whose height matches at least one (1) of the parameter ints
	 *
	 * @param heights the heights to compare component to
	 * @return filtered query
	 */
	public Components height(final int... heights) {
		return select(c -> {
			final int height = c.height();
			for (final int h : heights) {
				if (height == h) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Filters the current query's contents to only include components whose scroll width matches at least one (1) of the parameter ints
	 *
	 * @param widths the scroll widths to compare component to
	 * @return filtered query
	 */
	public Components scrollWidth(final int... widths) {
		return select(c -> {
			final int width = c.scrollWidth();
			for (final int w : widths) {
				if (width == w) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Filters the current query's contents to only include components whose scroll height matches at least one (1) of the parameter ints
	 *
	 * @param heights the scroll heights to compare component to
	 * @return filtered query
	 */
	public Components scrollHeight(final int... heights) {
		return select(c -> {
			final int height = c.scrollHeight();
			for (final int h : heights) {
				if (height == h) {
					return true;
				}
			}
			return false;
		});
	}
	/**
	 * Filters the current query's contents to only include components whose id matches at least one (1) of the parameter ints
	 *
	 * @param ids the ids to compare component to
	 * @return filtered query
	 */
	public Components id(final int... ids) {
		return select(component -> {
			final int compId = component.id();
			for (final int id : ids) {
				if (compId == id) {
					return true;
				}
			}
			return false;
		});
	}

	@Override
	public ComponentStream toStream() {
		return new ComponentStream(ctx, getInternal().stream());
	}
}
