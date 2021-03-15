package org.powerbot.script.action;

import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.script.rt4.ClientContext;

import java.util.Queue;

public interface ActionEmitter {

	void emit(AbstractAction action);

	void emit(IClient client, AbstractAction action);

	void processQueue();

	void setClientContext(ClientContext clientContext);
}
