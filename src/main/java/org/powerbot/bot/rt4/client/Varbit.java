package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.HashTable;
import org.powerbot.bot.rt4.client.internal.IHashTable;
import org.powerbot.bot.rt4.client.internal.IVarbit;

public class Varbit extends Node<IVarbit> {

	public Varbit(final IVarbit wrapped) {
		super(wrapped);
	}

	public int getIndex() {
		if (!isNull()) {
			return get().getIndex();
		}

		return -1;
	}

	public int getStartBit() {
		if (!isNull()) {
			return get().getStartBit();
		}

		return -1;
	}

	public int getEndBit() {
		if (!isNull()) {
			return get().getEndBit();
		}

		return -1;
	}

}
