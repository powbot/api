 package org.powerbot.bot;


import org.powerbot.script.ClientContext;
import org.powerbot.script.GameActionEvent;

import java.io.Closeable;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public abstract class EventDispatcher extends AbstractCollection<EventListener> implements Runnable, Closeable {
	private final CopyOnWriteArrayList<EventListener> listeners;
	protected final Map<EventListener, List<Integer>> listenersToId;
	private final BlockingQueue<EventObject> queue;
	protected final Map<Class<? extends EventListener>, Integer> masks;

	protected EventDispatcher() {
		listeners = new CopyOnWriteArrayList<>();
		listenersToId = new ConcurrentHashMap<>();
		queue = new LinkedBlockingQueue<>();
		masks = new HashMap<>();
	}

	private List<Integer> getMasks(final EventListener e) {
		return masks.entrySet().stream()
			.filter(es -> es.getKey().isInstance(e)).map(Map.Entry::getValue).collect(Collectors.toList());
	}

	public final void dispatch(final EventObject e) {
		queue.offer(e);
	}

	public final void consume(final EventObject e) {
		consume(e, getType(e));
	}

	protected abstract void consume(final EventObject e, final int t);

	protected abstract int getType(final EventObject e);

	@Override
	public final void run() {
		while (!ClientContext.ctx().controller.isStopping()) {
			final EventObject o;

			try {
				o = queue.take();
			} catch (final InterruptedException ignored) {
				break;
			}

			try {
				consume(o);
			} catch (final Throwable ignored) {
			}
		}
	}

	@Override
	public final void close() {
	}

	@Override
	public final Iterator<EventListener> iterator() {
		final Iterator<EventListener> e = listeners.iterator();
		return new Iterator<EventListener>() {
			private volatile EventListener o = null;

			@Override
			public boolean hasNext() {
				return e.hasNext();
			}

			@Override
			public EventListener next() {
				o = e.next();
				return o;
			}

			@Override
			public void remove() {
				if (o == null) {
					throw new IllegalStateException();
				}
				EventDispatcher.this.remove(o);
				o = null;
			}
		};
	}

	@Override
	public final int size() {
		return listeners.size();
	}

	@Override
	public final boolean add(final EventListener e) {
		if (listeners.addIfAbsent(e)) {
			listenersToId.put(e, getMasks(e));
			return true;
		}

		return false;
	}

	public final boolean remove(final EventListener e) {
		if (listeners.remove(e)) {
			listenersToId.remove(e);
			return true;
		}

		return false;
	}

	public final boolean contains(final Class<? extends EventListener> o) {
		for (final EventListener e : listeners) {
			if (e.getClass().isAssignableFrom(o)) {
				return true;
			}
		}
		return false;
	}
}
