package org.powerbot.script;

/**
 * A background process for handling specific in-game conditions not otherwise dealt with by scripts
 *
 * @param <C> the client context
 */
public abstract class Daemon<C extends ClientContext> {

	/**
	 * The {@link org.powerbot.script.ClientContext} for accessing client data.
	 */
	protected final C ctx;

	public Daemon(C ctx) {
		this.ctx = ctx;
	}

	public abstract String name();

	public abstract String description();

	/**
	 * Checks if the daemon should wake up
	 *
	 * @return should execute
	 */
	public abstract boolean check();

	/**
	 * Wakes up the daemon and returns a success flag
	 *
	 * @return success flag
	 */
	public abstract boolean execute();

}
