package org.powerbot.bot.rt4;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

public class RandomEvents extends PollingScript<ClientContext> {
	public RandomEvents() {
		priority.set(3);
	}

	private boolean isValid() {
    	return ctx.input.blocking() && !ctx.properties.getProperty("randomevents.disable", "").equals("true") 
			&& !ctx.npcs.select().within(5d).action("Dismiss").select(npc -> npc.interacting().equals(ctx.players.local()) 
					&& npc.tile().matrix(ctx).reachable()).isEmpty();
	}

	@Override
	public void poll() {
		if (!isValid()) {
			threshold.remove(this);
			return;
		}
		threshold.add(this);
		
		 if(ctx.inventory.selectedItemIndex() >= 0){
			ctx.inventory.selectedItem().click();
		}
		
		final Npc npc = ctx.npcs.poll();
		if(ctx.input.move(npc.nextPoint())) {
			for(final MenuCommand a : ctx.menu.commands()) {
				if(!a.option.contains(" -> "))
					continue;

				ctx.widgets.component(Constants.CHAT_WIDGET, Constants.CHAT_VIEWPORT).click();
			}
		}
		if (npc.interact(false, "Dismiss")) {
			Condition.wait(new Condition.Check() {
				@Override
				public boolean poll() {
					return !npc.valid();
				}
			}, 300, 12);
		}
	}
}
