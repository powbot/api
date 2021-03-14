package org.powerbot.bot.rt4;

import org.powerbot.bot.rt4.client.internal.IWidget;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

public class BankPin extends Daemon<ClientContext> {
	private int count = 0;

	public BankPin(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public String name() {
		return "BankPin";
	}

	@Override
	public String description() {
		return "Enters your Bank PIN into the dialog";
	}

	@Override
	public boolean check() {
		return ctx.widgets.widget(Constants.BANKPIN_WIDGET).valid();
	}

	@Override
	public boolean execute() {
		if(check()) {
			final String pin = ctx.getPin();
			if (pin == null) {
				ctx.controller.stop();
				return false;
			}

			final int preCount = count;
			final Widget w = ctx.widgets.widget(Constants.BANKPIN_WIDGET);

			for (final IWidget c : w.components()) {
				if (c.textColor() != 0 || c.width() != 64 || c.height() != 64 || c.componentCount() != 2 || !c.visible()) {
					continue;
				}
				final IWidget child = c.component(1);
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
			return true;
		}
		return false;
	}
}
