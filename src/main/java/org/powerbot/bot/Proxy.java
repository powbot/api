package org.powerbot.bot;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Proxy<T> {
	private final WeakReference<T> wrapped;
	private final T obj;

	private static final Map<Class<? extends Proxy>, Class> typeCache = new HashMap<>();

	public Proxy(final T wrapped) {
		this(wrapped, wrapped != null && !wrapped.getClass().getCanonicalName().contains("Impl"));
	}

	public Proxy(final T wrapped, final boolean wrap) {
		if (wrap) {
			this.wrapped = new WeakReference<>(wrapped);
			this.obj = null;
		} else {
			this.obj = wrapped;
			this.wrapped = null;
		}

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
		if (wrapped != null)
			return System.identityHashCode(wrapped.get());
		if (obj != null)
			return obj.hashCode();

		return 0;
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof Proxy)) {
			return false;
		}
		if (!isNull() && !((Proxy) o).isNull()) {
			final T unwrapped = get();
			return unwrapped == ((Proxy) o).get();
		}

		return this == o;
	}

	public T get() {
		return wrapped != null ? wrapped.get() : obj;
	}
}
