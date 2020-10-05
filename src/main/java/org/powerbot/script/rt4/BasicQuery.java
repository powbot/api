package org.powerbot.script.rt4;

import org.powerbot.script.*;

import java.util.regex.Pattern;

/**
 * BasicQuery
 * A basic query for all entities within the viewport.
 *
 * @param <K> the type of entity within the viewport
 */
public abstract class BasicQuery<K extends Locatable & Identifiable & Nameable & Viewable & Actionable> extends AbstractQuery<BasicQuery<K>, K, org.powerbot.script.rt4.ClientContext>
		implements Locatable.Query<BasicQuery<K>>, Identifiable.Query<BasicQuery<K>>,
		Nameable.Query<BasicQuery<K>>, Viewable.Query<BasicQuery<K>>, Actionable.Query<BasicQuery<K>> {
	public BasicQuery(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BasicQuery<K> getThis() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BasicQuery<K> at(final Locatable l) {
		return select(new Locatable.Matcher(l.tile()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BasicQuery<K> within(final double radius) {
		return within(ctx.players.local().tile(), radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BasicQuery<K> within(final Locatable locatable, final double radius) {
		return select(new Locatable.WithinRange(locatable.tile(), radius));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BasicQuery<K> within(final Area area) {
		return select(new Locatable.WithinArea(area));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BasicQuery<K> nearest() {
		return nearest(ctx.players.local().tile());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BasicQuery<K> nearest(final Locatable locatable) {
		return sort(new Locatable.NearestTo(locatable.tile()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BasicQuery<K> id(final int... ids) {
		return select(new Identifiable.Matcher(ids));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BasicQuery<K> name(final Pattern... names) {
		return select(new Nameable.Matcher(names));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BasicQuery<K> action(final Pattern... actions) {
		return select(new Actionable.Matcher(actions));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BasicQuery<K> viewable() {
		//DO NOT REPLACE WITH METHOD REFERENCE
		//see #2119
		return select(k -> k.inViewport());
	}
}
