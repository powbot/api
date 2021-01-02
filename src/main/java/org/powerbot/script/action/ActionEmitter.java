package org.powerbot.script.action;

import org.powerbot.bot.rt4.client.internal.IClient;

import java.util.Queue;

public interface ActionEmitter {

	void emit(IClient client, AbstractAction action);

	void processQueue();

	Queue<AbstractAction> queue();
}
