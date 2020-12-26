package org.powerbot.bot.rt4;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

public class CanContinue extends Daemon<ClientContext> {

	public CanContinue(ClientContext ctx) {
		super(ctx);
	}

	private Component getContinueWidget() {
		return ctx.widgets.component(193, 0, 2);
	}

	@Override
	public String name() {
		return "CanContinue";
	}

	@Override
	public String description() {
		return "Clicks continue.";
	}

	@Override
	public boolean check() {
		return getContinueWidget().valid();
	}

	@Override
	public boolean execute() {
		return getContinueWidget().valid() && getContinueWidget().click();
	}
}
