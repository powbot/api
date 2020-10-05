package org.powerbot.script.rt4;

import org.powerbot.script.*;

import java.util.regex.Pattern;

/**
 * PlayerQuery
 *
 * @param <K> the type of query which must extend any one of the specified types
 */
public abstract class PlayerQuery<K extends Locatable & Nameable & Viewable> extends AbstractQuery<PlayerQuery<K>, K, org.powerbot.script.rt4.ClientContext>
		implements Locatable.Query<PlayerQuery<K>>, Nameable.Query<PlayerQuery<K>>, Viewable.Query<PlayerQuery<K>> {
	protected PlayerQuery(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PlayerQuery<K> getThis() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> at(final Locatable l) {
		return select(new Locatable.Matcher(l));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> within(final double radius) {
		return within(ctx.players.local(), radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> within(final Locatable locatable, final double radius) {
		return select(new Locatable.WithinRange(locatable, radius));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> within(final Area area) {
		return select(new Locatable.WithinArea(area));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> nearest() {
		return nearest(ctx.players.local());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> nearest(final Locatable locatable) {
		return sort(new Locatable.NearestTo(locatable));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> name(final Pattern... names) {
		return select(new Nameable.Matcher(names));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerQuery<K> viewable() {
		return select(Viewable::inViewport);
	}
}
