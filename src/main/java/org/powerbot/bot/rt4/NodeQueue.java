package org.powerbot.bot.rt4;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.internal.INode;
import org.powerbot.bot.rt4.client.internal.INodeDeque;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class NodeQueue {
	public static <E> List<E> get(final INodeDeque q, final Class<E> type) {
		final List<E> list = new ArrayList<>();

		INode e;
		final INode s;
		if (q == null || (s = e = q.getSentinel()) == null) {
			return list;
		}
		e = e.getNext();

		for (; e != null && type.isAssignableFrom(s.getClass()) && !e.equals(s); e = e.getNext()) {
			E obj = (E) e;
			if (obj != null) {
				list.add(obj);
			}
		}

		return list;
	}
}
