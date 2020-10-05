package org.powerbot.script.rt4;

import org.powerbot.script.*;

/**
 * IdQuery
 *
 * @param <K> the type of query which must extend {@link Identifiable}
 */
public abstract class IdQuery<K extends Identifiable> extends AbstractQuery<IdQuery<K>, K, ClientContext>
		implements Identifiable.Query<IdQuery<K>> {

	public IdQuery(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IdQuery<K> getThis() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IdQuery<K> id(final int... ids) {
		return select(new Identifiable.Matcher(ids));
	}
}
