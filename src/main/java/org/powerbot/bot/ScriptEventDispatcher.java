package org.powerbot.bot;

import org.powerbot.script.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ScriptEventDispatcher<C extends Client, E extends EventListener> extends AbstractCollection<E> {
	private final ClientContext<C> ctx;
	private final Collection<E> c;

	public ScriptEventDispatcher(final ClientContext<C> ctx) {
		this.ctx = ctx;
		c = new ArrayList<>();
	}

	@Override
	public boolean add(final E e) {
		return ctx.bot().getDispatcher().add(e) && c.add(e);
	}

	@Override
	public Iterator<E> iterator() {
		return new ListIterator<>(ctx, c.iterator());
	}

	@Override
	public int size() {
		return c.size();
	}

	private final class ListIterator<C1 extends C, E1 extends E> implements Iterator<E1> {
		private final ClientContext<C1> ctx;
		private final Iterator<E1> iterator;
		private final AtomicReference<E1> ref;

		ListIterator(final ClientContext<C1> ctx, final Iterator<E1> iterator) {
			this.ctx = ctx;
			this.iterator = iterator;
			ref = new AtomicReference<>(null);
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public E1 next() {
			ref.set(iterator.next());
			return ref.get();
		}

		@Override
		public void remove() {
			if (ref.get() == null) {
				throw new IllegalStateException();
			}
			ctx.bot().getDispatcher().remove(ref.getAndSet(null));
			iterator.remove();
		}
	}
}
