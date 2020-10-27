package org.powerbot.bot.rt4;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

public class BankPinPending extends PollingScript<ClientContext> {
	public BankPinPending() {
		priority.set(4);
	}

	@Override
	public void poll() {
		final Component component = ctx.widgets.component(Constants.BANKPIN_PENDING_WIDGET, Constants.BANKPIN_PENDING_COMPONENT);
		if (!component.valid()) {
			threshold.remove(this);
			return;
		}
		threshold.add(this);

		component.click();
	}
}
