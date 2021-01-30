package org.powerbot.script.rt4;

import org.powerbot.script.*;

/**
 * TextQuery
 *
 * @param <K> the type of query which must extend {@link Textable}
 */
@Deprecated
public abstract class TextQuery<K extends Textable> extends AbstractQuery<TextQuery<K>, K, ClientContext>
		implements Textable.Query<TextQuery<K>> {
	public TextQuery(final ClientContext ctx) {
		super(ctx);
	}

	@Deprecated
	@Override
	protected TextQuery<K> getThis() {
		return this;
	}

	@Deprecated
	@Override
	public TextQuery<K> text(final String... texts) {
		return select(new Textable.Matcher(texts));
	}
}
