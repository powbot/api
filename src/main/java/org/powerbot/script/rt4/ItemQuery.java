package org.powerbot.script.rt4;

import org.powerbot.script.*;

import java.util.regex.Pattern;

/**
 * ItemQuery
 *
 * @param <K> the type of query which must extend any one of the specified types
 */
public abstract class ItemQuery<K extends Identifiable & Nameable & Stackable & Viewable & Actionable> extends AbstractQuery<ItemQuery<K>, K, ClientContext>
		implements Identifiable.Query<ItemQuery<K>>, Nameable.Query<ItemQuery<K>>, Stackable.Query<ItemQuery<K>>, Actionable.Query<ItemQuery<K>>, Viewable.Query<ItemQuery<K>> {
	public ItemQuery(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ItemQuery<K> getThis() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemQuery<K> id(final int... ids) {
		return select(new Identifiable.Matcher(ids));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemQuery<K> name(final Pattern... names) {
		return select(new Nameable.Matcher(names));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemQuery<K> action(final Pattern... actions) {
		return select(new Actionable.Matcher(actions));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemQuery<K> viewable() {
		return select(Viewable::inViewport);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int count() {
		return size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int count(final boolean stacks) {
		if (!stacks) {
			return count();
		}
		int count = 0;
		for (final Stackable stackable : this) {
			count += stackable.stackSize();
		}
		return count;
	}
}
