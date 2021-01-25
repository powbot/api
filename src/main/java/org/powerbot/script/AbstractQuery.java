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
	private final ThreadLocal<List<Filter<? super K>>> filters;

	/**
	 * Creates a base {@link AbstractQuery}.
	 *
	 * @param ctx the {@link ClientContext} to associate with
	 */
	public AbstractQuery(final C ctx) {
		super(ctx);

		items = ThreadLocal.withInitial(() -> new ArrayList<>(AbstractQuery.this.get()));
		filters = ThreadLocal.withInitial(ArrayList::new);
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
		final List<K> a = get();
		items.set(a);
		filters.set(new ArrayList<>());
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
		final List<K> a = new ArrayList<>();
		for (final K k : c) {
			a.add(k);
		}
		items.set(a);
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
		filters.get().add(f);

		if (items.get().isEmpty()) {
			select();
		}

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
		final List<K> a = new ArrayList<>(items.get());
		a.sort(c);
		items.set(a);
		return getThis();
	}

	/**
	 * Sorts the items in the query cache by a random rearrangement.
	 *
	 * @return {@code this} for the purpose of chaining
	 */
	public T shuffle() {
		final List<K> a = new ArrayList<>(items.get());
		Collections.shuffle(a);
		items.set(a);
		return getThis();
	}

	/**
	 * Reverses the order of items in the query cache.
	 *
	 * @return {@code this} for the purpose of chaining
	 */
	public T reverse() {
		final List<K> a = new ArrayList<>(items.get());
		Collections.reverse(a);
		items.set(a);
		return getThis();
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
	public T limit(int offset, final int count) {
		final List<K> items = this.items.get();
		if (!items.isEmpty()) {
			final int c = Math.min(offset + count, items.size());
			if (offset >= items.size()) {
				offset = 0;
			}

			this.items.set(items.subList(offset, c));
		}
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
		getFiltered().forEach(c::add);
		return getThis();
	}

	private Filter<? super K> mergeFilters() {
		final List<Filter<? super K>> filters = this.filters.get();

		return (Filter<K>) k -> {
			for (Filter<? super K> filter : filters) {
				if (!filter.accept(k)) {
					return false;
				}
			}
			return true;
		};
	}

	private Stream<K> getFiltered() {
		final Filter<? super K> filter = mergeFilters();

		return items.get().stream().filter(filter::accept);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<K> iterator() {
		final Iterator<K> i = getFiltered()
			.iterator();

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
		final Stream<K> items = getFiltered();
		return items.findFirst().map(item -> {
			limit(1, this.items.get().size() - 1);
			return item;
		}).orElse(nil());
	}

	/**
	 * Retrieves, but does not remove, the first item in the query cache, or returns the value of {@link #nil()} if it is empty.
	 *
	 * @return the first item in the query cache, or the value of {@link #nil()} if it is empty
	 */
	public K peek() {
		final Stream<K> items = getFiltered();
		return items.findFirst().orElse(nil());
	}

	/**
	 * Enumerates through each item in the query cache.
	 *
	 * @param c the handler for each iteration, which should return {@code false} to break iteration
	 * @return {@code this} for the purpose of chaining
	 */
	public T each(final Filter<? super K> c) {
		getFiltered().takeWhile(c::accept);

		return getThis();
	}

	/**
	 * Returns {@code true} if the query cache contains no items.
	 *
	 * @return {@code true} if the query cache contains no items
	 */
	public boolean isEmpty() {
		return size() > 0;
	}

	/**
	 * Returns {@code true} if the query cache contains the specified item.
	 *
	 * @param k item whose presence in this query cache is to be tested
	 * @return {@code true} if the query cache contains the specified item
	 */
	public boolean contains(final K k) {
		return getFiltered().anyMatch(i -> i == k || i.equals(k));
	}

	/**
	 * Returns the number of items in the query cache.
	 *
	 * @return the number of items in the query cache
	 */
	public int size() {
		return (int) getFiltered().count();
	}
}
