package org.powerbot.script;

import org.powerbot.bot.rt4.client.internal.IWidget;

import java.util.EventListener;

public interface WidgetPositionListener extends EventListener {

	void onWidgetPosition(final IWidget widget, final int x, final int y);
}
