package org.powerbot.script;

/**
 * Suspendable
 * An object which can be suspended and restarting and checked for its current status.
 */
public interface Suspendable {
	/**
	 * Whether or not the object is suspended from operation.
	 *
	 * @return {@code true} is the object is not in operation; {@code false} otherwise.
	 */
	boolean isSuspended();

	/**
	 * Attempts to suspend operation of the object.
	 */
	void suspend();

	/**
	 * Attempts to resume operation of the object.
	 */
	void resume();
}
