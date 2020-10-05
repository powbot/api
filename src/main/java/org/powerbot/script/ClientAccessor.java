package org.powerbot.script;

/**
 * ClientAccessor
 * A base class that requires a {@link org.powerbot.script.ClientContext}.
 *
 * @param <T> the type of context
 */
public abstract class ClientAccessor<T extends ClientContext> {
	/**
	 * The context object.
	 */
	public final T ctx;

	/**
	 * Creates a new instance with the specified context.
	 *
	 * @param ctx the context
	 */
	public ClientAccessor(final T ctx) {
		this.ctx = ctx;
	}
}
