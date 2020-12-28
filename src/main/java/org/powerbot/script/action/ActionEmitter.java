package org.powerbot.script.action;

import org.powerbot.bot.rt4.client.internal.IClient;

public interface ActionEmitter {

	public void emit(IClient client, AbstractAction action);

}
