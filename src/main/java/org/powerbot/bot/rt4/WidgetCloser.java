package org.powerbot.bot.rt4;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WidgetCloser extends Daemon<ClientContext> {
	private final Map<Integer, AtomicInteger> attempts;

	public WidgetCloser(ClientContext ctx) {
		super(ctx);
		attempts = new HashMap<>();
		for (final int i : Constants.WIDGETCLOSER_TRADE_ITEMS) {
			attempts.put(i, new AtomicInteger(0));
		}
	}

	@Override
	public String name() {
		return "Widget Closer";
	}

	@Override
	public String description() {
		return "Closes Widgets";
	}

	@Override
	public boolean check() {
		return Arrays.stream(Constants.WIDGETCLOSER_TRADE_ITEMS)
			.filter(it -> {
				final Component c = ctx.widgets.component(it >> 16, it & 0xffff);
				return c.valid() && c.visible();
			}).count() > 0L;
	}

	@Override
	public boolean execute() {
		final List<Integer> w = new ArrayList<>();

		if (!ctx.bot().allowTrades()) {
			for (final int e : Constants.WIDGETCLOSER_TRADE_ITEMS) {
				w.add(e);
			}
		}

		for (final int id : w) {
			final AtomicInteger a = attempts.get(id);
			if (a.get() >= 3) {
				continue;
			}

			final Component c = ctx.widgets.component(id >> 16, id & 0xffff);
			final Point p = c.screenPoint();
			if (c.visible() && c.click()) {
				if (Condition.wait(new Condition.Check() {
					@Override
					public boolean poll() {
						return !c.visible() || !c.screenPoint().equals(p);
					}
				})) {
					a.set(0);
				} else {
					a.incrementAndGet();
				}
			}
		}
		return false;
	}
}
