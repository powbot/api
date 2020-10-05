package org.powerbot.script;

import java.util.EventListener;

/**
 * MessageListener
 * A listener for chat box traffic.
 */
public interface MessageListener extends EventListener {
	/**
	 * Response fired upon the chat box receiving a new message.
	 *
	 * @param event the event representing the message
	 */
	void messaged(MessageEvent event);
}
