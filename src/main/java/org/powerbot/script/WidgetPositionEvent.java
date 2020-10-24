package org.powerbot.script;

import org.powerbot.bot.AbstractEvent;
import org.powerbot.bot.EventType;
import org.powerbot.bot.rt4.client.internal.IWidget;

import java.util.EventListener;

public class WidgetPositionEvent extends AbstractEvent {
	private static final long serialVersionUID = -5981584965478127864L;
	public static final int EVENT_ID = EventType.WIDGET_POSITION_EVENT.id();
	private final IWidget widget;
	private final int x;
	private final int y;


	public WidgetPositionEvent(final IWidget widget, final int x, final int y) {
		super(EVENT_ID);
		this.widget = widget;
		this.x = x;
		this.y = y;
	}

	public IWidget getWidget() {
		return widget;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public void call(final EventListener e) {
		try {
			((WidgetPositionListener) e).onWidgetPosition(widget, x, y);
		} catch (final Exception ignored) {
		}
	}
}
