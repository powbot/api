package org.powerbot.bot.rt4;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

public class RandomEvents extends Daemon<ClientContext> {

	public RandomEvents(ClientContext ctx) {
		super(ctx);
	}

	private boolean isValid() {
		return ctx.input.blocking() &&
			!ctx.properties.getProperty("randomevents.disable", "").equals("true")
			&& !ctx.npcs.select().within(5d).action("Dismiss").select(npc -> npc.interacting().equals(ctx.players.local())
			&& npc.tile().matrix(ctx).reachable()).isEmpty();
	}

	@Override
	public String name() {
		return "RandomEvents";
	}

	@Override
	public String description() {
		return "Dismisses Random Event NPCs";
	}

	@Override
	public boolean check() {
		return isValid();
	}

	@Override
	public boolean execute() {
		if (isValid()) {
			if (ctx.inventory.selectedItemIndex() >= 0) {
				ctx.inventory.selectedItem().click();
			}

			final Npc npc = ctx.npcs.poll();
			if (ctx.input.move(npc.nextPoint())) {
				for (final MenuCommand a : ctx.menu.commands()) {
					if (!a.option.contains(" -> "))
						continue;

					ctx.widgets.component(Constants.CHAT_WIDGET, Constants.CHAT_VIEWPORT).click();
				}
			}

			if (npc.interact(false, "Dismiss")) {
				return Condition.wait(new Condition.Check() {
					@Override
					public boolean poll() {
						return !npc.valid();
					}
				}, 300, 12);
			}
		}
		return false;
	}
}
