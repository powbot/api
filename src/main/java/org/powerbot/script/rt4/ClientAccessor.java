package org.powerbot.script.rt4;

/**
 * ClientAccessor
 * An object with a reference to a ClientContext
 *
 * @see ClientContext
 */
public abstract class ClientAccessor extends org.powerbot.script.ClientAccessor<ClientContext> {

	public ClientAccessor(final ClientContext ctx) {
		super(ctx);
	}

	public ClientContext ctx() {
		return ctx;
	}
}
