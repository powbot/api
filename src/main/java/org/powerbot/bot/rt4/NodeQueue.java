package org.powerbot.bot.rt4;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class NodeQueue {
	public static <E extends Proxy> List<E> get(final NodeDeque q, final Class<E> type) {
		final List<E> list = new ArrayList<>();

		Node e;
		final Node s;
		if (q == null || (s = e = q.getSentinel()) == null) {
			return list;
		}
		e = e.getNext();

		for (; !e.isNull() && e.isTypeOf(type) && !e.equals(s); e = e.getNext()) {
			E obj = IteratorUtils.newInstance(e, type);
			if (obj != null) {
				list.add(obj);
			}
		}

		return list;
	}
}
