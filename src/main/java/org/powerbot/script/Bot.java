package org.powerbot.script;

import org.powerbot.bot.*;
import org.powerbot.bot.cache.*;
import org.powerbot.script.action.ActionEmitter;

import java.applet.Applet;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * Bot
 * An intermediary object containing references to the client applet, endpoints, and event dispatchers.
 *
 * @param <C> the type of context
 */
public abstract class Bot<C extends ClientContext<? extends Client>> {
	public final C ctx;
	protected final Logger log = Logger.getLogger("Bot");

	public Bot() {
		ctx = newContext();
	}

	protected abstract C newContext();

	public abstract boolean allowTrades();

	public abstract void openURL(final String u);

	public abstract Type getScriptTypeArg(final Class<? extends AbstractScript> c);

	public abstract Class<?> getPrimaryClientContext(final Class<?> c);

	public abstract EventDispatcher getDispatcher();

	public abstract void update();

	@SuppressWarnings("deprecation")
	public abstract Applet getApplet();

	public abstract Input newInput();

	public abstract Script.Controller newScriptController(final ClientContext<? extends Client> ctx);

	public abstract Collection<? extends Class<? extends Script>> listDaemons();

	public abstract AbstractCacheWorker getCacheWorker();

	public abstract WebWalkingService getWebWalkingService();

	public abstract ActionEmitter getActionEmitter();

}
