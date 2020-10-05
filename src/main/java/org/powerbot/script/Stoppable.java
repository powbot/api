package org.powerbot.script;

/**
 * Stoppable
 * An object which can be stopped and checked for its current status.
 */
public interface Stoppable {
	/**
	 * Whether or not the object is currently attempting to stop.
	 *
	 * @return {@code true} is the object is attempting to stop; {@code false} otherwise.
	 */
	boolean isStopping();

	/**
	 * Attempts to stop this objects execution.
	 */
	void stop();
}
