package org.powerbot.bot.rt4;

import org.powerbot.bot.rt4.client.internal.IWidget;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

public class BankPinPending extends Daemon<ClientContext> {

	public BankPinPending(ClientContext ctx) {
		super(ctx);
	}

	private IWidget getPendingScreen() {
		return ctx.widgets.component(Constants.BANKPIN_PENDING_WIDGET, Constants.BANKPIN_PENDING_COMPONENT);
	}

	@Override
	public String name() {
		return "BankPinPending";
	}

	@Override
	public String description() {
		return "Closes the bank pin pending screen.";
	}

	@Override
	public boolean check() {
		return getPendingScreen().valid();
	}

	@Override
	public boolean execute() {
		return getPendingScreen().valid() && getPendingScreen().click();
	}
}
