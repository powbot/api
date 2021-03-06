package org.powerbot.script;

import org.powerbot.bot.AbstractEvent;
import org.powerbot.bot.EventType;
import org.powerbot.bot.rt4.client.internal.IMessageEntry;

import java.util.EventListener;

/**
 * MessageEvent
 * An event that represents a message sent by the game to the chat box.
 */
public class MessageEvent extends AbstractEvent {
	public static final int EVENT_ID = EventType.MESSAGE_EVENT.id();
	private static final long serialVersionUID = 4178447203851407187L;
	private final int id;
	private final String source, message;

	public MessageEvent(final IMessageEntry entry) {
		this(entry.getType(), entry.getSender(), entry.getMessage());
	}

	public MessageEvent(final int id, final String source, final String message) {
		super(EVENT_ID);
		this.id = id;
		this.source = source;
		this.message = message;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void call(final EventListener eventListener) {
		((MessageListener) eventListener).messaged(this);
	}

	/**
	 * @return the id of this message.
	 */
	public int type() {
		return id;
	}


	/**
	 * @return the name of the sender of this message
	 */
	public String source() {
		return source;
	}

	/**
	 * @return the contents of this message
	 */
	public String text() {
		return message;
	}

	@Override
	public String toString() {
		return String.format("(%d) [%s]: %s%n", id, source, message);
	}
}
