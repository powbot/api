package org.powerbot.bot.rt4;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

public class BankPin extends PollingScript<ClientContext> {
	public BankPin() {
		priority.set(2);
	}

	private int count = 0;

	@Override
	public void poll() {
		if (!ctx.widgets.widget(Constants.BANKPIN_WIDGET).valid()) {
			threshold.remove(this);
			return;
		}
		threshold.add(this);

		final String pin = ctx.getPin();
		if (pin == null) {
			ctx.controller.stop();
			return;
		}

		final int preCount = count;
		final Widget w = ctx.widgets.widget(Constants.BANKPIN_WIDGET);

		for (final Component c : w.components()) {
			if (c.textColor() != 0 || c.width() != 64 || c.height() != 64 || c.componentCount() != 2 || !c.visible()) {
				continue;
			}
			final Component child = c.component(1);
			if (!child.visible()) {
				continue;
			}
			//TODO: re-evaluate this to get rid of count; or fail out
			final String text = child.text();
			if (text.equals("" + pin.charAt(count % 4))) {
				if (c.click()) {
					count++;
					Condition.wait(new Condition.Check() {
						public boolean poll() {
							return !child.text().equals(text);
						}
					}, 100, 20);
				}
			}
		}
		if (preCount == count) {
			w.component(Random.nextInt(0, ctx.widgets.widget(Constants.BANKPIN_WIDGET).componentCount())).hover();
		}
	}
}
