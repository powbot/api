package org.powerbot.bot.rt4.client.internal;

public interface IMessageEntry extends IEntry {

	String getMessage();

	String getSender();

	int getType();

}