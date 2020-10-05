package org.powerbot.bot;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Proxy<T> {
	public final WeakReference<T> wrapped;

	private static final Map<Class<? extends Proxy>, Class> typeCache = new HashMap<>();

	public Proxy(final T wrapped) {
		this.wrapped = new WeakReference<>(wrapped);
	}

	private static Class<?> getTypeClass(final Class<? extends Proxy> c) {
		return Arrays.stream(c.getConstructors()).
			filter(constructor -> constructor.getParameterCount() == 1
				&& constructor.getParameterTypes()[0].getName().startsWith("org.powerbot.bot.rt4.client.internal"))
			.findFirst()
			.map(constructor -> constructor.getParameterTypes()[0].getClass())
			.orElse(null);
	}

	public boolean isTypeOf(final Class<? extends Proxy> c) {
		if (!typeCache.containsKey(c)) {
			typeCache.put(c, getTypeClass(c));
		}
		if (!typeCache.containsKey(getClass())) {
			typeCache.put(getClass(), getTypeClass(getClass()));
		}

		final Class<T> thisClass = typeCache.get(getClass());
		final Class<?> otherClass = typeCache.get(c);

		return thisClass != null && otherClass != null && thisClass.isAssignableFrom(otherClass);
	}

	public boolean isNull() {
		return get() == null;
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(wrapped.get());
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof Proxy)) {
			return false;
		}
		if (!isNull() && !((Proxy) o).isNull()) {
			final T unwrapped = this.wrapped.get();
			return unwrapped == ((Proxy) o).wrapped.get();
		}

		return this == o;
	}

	public T get() {
		return wrapped != null ? wrapped.get() : null;
	}
}
