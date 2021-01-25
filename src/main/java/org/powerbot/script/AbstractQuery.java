package org.powerbot.script;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * AbstractQuery
 * An abstract implementation of a chaining query-based data set filter which is thread safe.
 *
 * @param <T> the super class
 * @param <K> the subject type
 * @param <C> the {@link ClientContext}
 */
public abstract class AbstractQuery<T extends AbstractQuery<T, K, C>, K, C extends ClientContext> extends ClientAccessor<C> implements Supplier<List<K>>, Iterable<K>, Nillable<K> {
	private final ThreadLocal<List<K>> items;

	/**
	 * Creates a base {@link AbstractQuery}.
	 *
	 * @param ctx the {@link ClientContext} to associate with
	 */
	public AbstractQuery(final C ctx) {
		super(ctx);

		items = ThreadLocal.withInitial(() -> new CopyOnWriteArrayList<>(AbstractQuery.this.get()));
	}

	/**
	 * Returns {@code this}.
	 *
	 * @return must always return {@code this}
	 */
	protected abstract T getThis();

	/**
	 * Returns a fresh data set.
	 *
	 * @return a new data set for subsequent queries
	 */
	public abstract List<K> get();

	/**
	 * Returns a {@link Stream} with this collection as its source.
	 *
	 * @return a sequential {@link Stream} over the elements in this collection
	 * @deprecated use {@link #get()} then {@link List#stream()}
	 */
	public final Stream<K> stream() {
		return get().stream();
	}

	/**
	 * Selects a fresh data set into the query cache.
	 *
	 * @return {@code this} for the purpose of chaining
	 */
	public T select() {
		final List<K> items = this.items.get(), a = get();
		setArray(items, a);
		return getThis();
	}

	/**
	 * Selects the specified data set into the query cache.
	 *
	 * @param c a {@link List}, {@link Collection} or any other {@link Iterable}
	 *          source of items to replace the existing cache with
	 * @return {@code this} for the purpose of chaining
	 */
	public T select(final Iterable<K> c) {
		final List<K> items = this.items.get(), a = new ArrayList<>();
		for (final K k : c) {
			a.add(k);
		}
		setArray(items, a);
		return getThis();
	}

	/**
	 * Selects the items which satisfy the condition of the specified
	 * {@link Filter} into the query cache.
	 *
	 * @param f the condition
	 * @return {@code this} for the purpose of chaining
	 */
	public T select(final Filter<? super K> f) {
		final List<K> items = this.items.get(), a = new ArrayList<>(items.size());
		for (final K k : items) {
			if (f.accept(k)) {
				a.add(k);
			}
		}
		setArray(items, a);
		return getThis();
	}

	/**
	 * Selects distinct elements
	 *
	 * @return {@code this}
	 */
	public T distinct() {
		return distinct(k -> k);
	}

	/**
	 * Selects distinct elements by a specific key
	 *
	 * @param function the applied function
	 * @return {@code this} for chaining
	 */
	public <Q> T distinct(Function<? super K, Q> function) {
		final Set<Q> distinct = new HashSet<>();
		return select(k -> {
			final Q applied = function.apply(k);
			if (!distinct.contains(applied)) {
				distinct.add(applied);
				return true;
			}
			return false;
		});
	}

	/**
	 * Sorts the items in the query cache by the specified {@link Comparator}.
	 *
	 * @param c the comparator
	 * @return {@code this} for the purpose of chaining
	 */
	public T sort(final Comparator<? super K> c) {
		final List<K> items = this.items.get(), a = new ArrayList<>(items);
		a.sort(c);
		setArray(items, a);
		return getThis();
	}

	/**
	 * Sorts the items in the query cache by a random rearrangement.
	 *
	 * @return {@code this} for the purpose of chaining
	 */
	public T shuffle() {
		final List<K> items = this.items.get(), a = new ArrayList<>(items);
		Collections.shuffle(a);
		setArray(items, a);
		return getThis();
	}

	/**
	 * Reverses the order of items in the query cache.
	 *
	 * @return {@code this} for the purpose of chaining
	 */
	public T reverse() {
		final List<K> items = this.items.get(), a = new ArrayList<>(items);
		Collections.reverse(a);
		setArray(items, a);
		return getThis();
	}

	private void setArray(final List<K> a, final List<K> c) {
		a.clear();
		a.addAll(c);
	}

	/**
	 * Limits the query cache to the specified number of items.
	 *
	 * @param count the maximum number of items to retain
	 * @return {@code this} for the purpose of chaining
	 */
	public T limit(final int count) {
		return limit(0, count);
	}

	/**
	 * Limits the query cache to the items within the specified bounds.
	 *
	 * @param offset the starting index
	 * @param count  the maximum number of items to retain
	 * @return {@code this} for the purpose of chaining
	 */
	public T limit(final int offset, final int count) {
		final List<K> items = this.items.get(), a = new ArrayList<>(count);
		final int c = Math.min(offset + count, items.size());
		for (int i = offset; i < c; i++) {
			a.add(items.get(i));
		}
		setArray(items, a);
		return getThis();
	}

	/**
	 * Limits the query cache to the first item (if any).
	 *
	 * @return {@code this} for the purpose of chaining
	 */
	public T first() {
		return limit(1);
	}

	/**
	 * Adds every item in the query cache to the specified {@link Collection}.
	 *
	 * @param c the {@link Collection} to add to
	 * @return {@code this} for the purpose of chaining
	 */
	public T addTo(final Collection<? super K> c) {
		c.addAll(items.get());
		return getThis();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<K> iterator() {
		final Iterator<K> i = items.get().iterator();

		return new Iterator<K>() {
			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public K next() {
				return hasNext() ? i.next() : nil();
			}

			@Override
			public void remove() {
				i.remove();
			}
		};
	}

	/**
	 * Retrieves and removes the first item in the query cache, or returns the value of {@link #nil()} if it is empty.
	 *
	 * @return the first item in the query cache, or the value of {@link #nil()} if it is empty
	 */
	public K poll() {
		final List<K> items = this.items.get();
		if (items.isEmpty()) {
			return nil();
		}
		final K v = items.get(0);
		limit(1, items.size() - 1);
		return v;
	}

	/**
	 * Retrieves, but does not remove, the first item in the query cache, or returns the value of {@link #nil()} if it is empty.
	 *
	 * @return the first item in the query cache, or the value of {@link #nil()} if it is empty
	 */
	public K peek() {
		final List<K> items = this.items.get();
		return items.isEmpty() ? nil() : items.get(0);
	}

	/**
	 * Enumerates through each item in the query cache.
	 *
	 * @param c the handler for each iteration, which should return {@code false} to break iteration
	 * @return {@code this} for the purpose of chaining
	 */
	public T each(final Filter<? super K> c) {
		for (final K k : this) {
			if (!c.accept(k)) {
				break;
			}
		}

		return getThis();
	}

	/**
	 * Returns {@code true} if the query cache contains no items.
	 *
	 * @return {@code true} if the query cache contains no items
	 */
	public boolean isEmpty() {
		return items.get().isEmpty();
	}

	/**
	 * Returns {@code true} if the query cache contains the specified item.
	 *
	 * @param k item whose presence in this query cache is to be tested
	 * @return {@code true} if the query cache contains the specified item
	 */
	public boolean contains(final K k) {
		return items.get().contains(k);
	}

	/**
	 * Returns the number of items in the query cache.
	 *
	 * @return the number of items in the query cache
	 */
	public int size() {
		return items.get().size();
	}
}
