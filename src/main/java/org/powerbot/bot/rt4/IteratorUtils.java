package org.powerbot.bot.rt4;

import org.powerbot.bot.Proxy;
import org.powerbot.bot.rt4.client.internal.INode;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class IteratorUtils {

	public static <T> T newInstance(final Proxy obj, final Class<T> type) {
		try {
			return (T) Arrays.stream(type.getDeclaredConstructors()).filter(c -> c.getParameterCount() == 1
				&& INode.class.isAssignableFrom(c.getParameterTypes()[0])).findFirst().orElse(null)
				.newInstance(obj != null ? obj.get() : null);
		} catch (final IllegalAccessException | InstantiationException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

}
