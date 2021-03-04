
package org.powerbot.script;

import org.powerbot.bot.*;

import java.util.Collection;
import java.util.EventListener;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * ClientContext
 * A context class which interlinks all core classes for a {@link org.powerbot.script.Bot}.
 *
 * @param <C> the bot client
 */
public abstract class ClientContext<C extends Client> {
	/**
	 * The script controller.
	 */
	public final Script.Controller controller;
	/**
	 * A table of key/value pairs representing environmental properties.
	 */
	public final Properties properties;
	/**
	 * A collection representing the event listeners attached to the {@link org.powerbot.script.Bot}.
	 */
	public final Collection<EventListener> dispatcher;
	/**
	 * The input simulator for sending keyboard and mouse events.
	 */
	public final Input input;

	private final AtomicReference<Bot<? extends ClientContext<C>>> bot;
	private final AtomicReference<C> client;

	private static AtomicReference<org.powerbot.script.rt4.ClientContext> ctx = new AtomicReference<>(null);

	/**
	 * Creates a new context with the given {@link org.powerbot.script.Bot}.
	 *
	 * @param bot the bot
	 */
	protected ClientContext(final Bot<? extends ClientContext<C>> bot) {
		this.bot = new AtomicReference<>(bot);
		client = new AtomicReference<>(null);
		controller = bot.newScriptController(this);
		properties = new Properties();
		dispatcher = new ScriptEventDispatcher<>(this);
		input = bot.newInput();

		properties.put("trades.allowed", "0");
		properties.put("sdn.host", "sdn.powbot.org");

		ClientContext.ctx.set((org.powerbot.script.rt4.ClientContext) this);
	}

	/**
	 * Creates a chained context.
	 *
	 * @param ctx the parent context
	 */
	protected ClientContext(final ClientContext<C> ctx) {
		bot = ctx.bot;
		client = ctx.client;
		controller = ctx.controller;
		properties = ctx.properties;
		dispatcher = ctx.dispatcher;
		input = ctx.input;

		if (ClientContext.ctx.get() == null) {
			ClientContext.ctx.set((org.powerbot.script.rt4.ClientContext) this);
		}
	}

	/**
	 * Returns the client version.
	 *
	 * @return the client version, which is {@code 6} for {@code rt6} and {@code} 4 for {@code rt4}
	 */
	public final String rtv() {
		final Class<?> c = getClass();
		if (org.powerbot.script.rt4.ClientContext.class.isAssignableFrom(c)) {
			return "4";
		}
		return "";
	}

	public static org.powerbot.script.rt4.ClientContext ctx() {
		return ctx.get();
	}

	/**
	 * Returns the bot.
	 *
	 * @return the bot
	 */
	public final Bot<? extends ClientContext<C>> bot() {
		return bot.get();
	}

	/**
	 * Returns the client.
	 *
	 * @return the client.
	 */
	public final C client() {
		return client.get();
	}

	/**
	 * Sets the client.
	 *
	 * @param c the new client
	 * @return the previous value, which may be {@code null}
	 */
	public final C client(final C c) {
		return client.getAndSet(c);
	}

	/**
	 * Returns the script controller.
	 *
	 * @return the script controller
	 * @deprecated use {@link #controller}
	 */
	@Deprecated
	public final Script.Controller controller() {
		return controller;
	}

	/**
	 * Returns the primary script.
	 *
	 * @param <T> the type of script
	 * @return the primary script, or {@code null} if one is not attached
	 * @deprecated use {@link org.powerbot.script.Script.Controller#script()}
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public final <T extends AbstractScript<? extends ClientContext<C>>> T script() {
		return (T) controller.script();
	}

	/**
	 * Returns the property value for the specified key, or an empty string as the default value.
	 *
	 * @param k the key to lookup
	 * @return the value for the specified key, otherwise an empty string if the requested entry does not exist
	 * @see #properties
	 */
	@Deprecated
	public final String property(final String k) {
		return property(k, "");
	}

	/**
	 * Returns the property value for the specified key, or a default value.
	 *
	 * @param k the key to lookup
	 * @param d the default value
	 * @return the value for the specified key, otherwise the default value if the requested entry does not exist
	 * @see #properties
	 */
	@Deprecated
	public final String property(final String k, final String d) {
		return properties.getProperty(k, d);
	}

	/**
	 * Returns the bank PIN for the currently logged in user.
	 *
	 * @return the PIN or {@code null} if unspecified
	 */
	public String getPin() {
		return properties.getProperty("login." + properties.getProperty("login.user") + ".pin");
	}
}
