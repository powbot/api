package org.powerbot.script;

/**
 * Validatable
 * A temporary object which can be removed from the game or refreshed.
 */
public interface Validatable {
	/**
	 * Whether or not the object still exists.
	 *
	 * @return {@code true} if the object still exists; {@code false} otherwise.
	 */
	boolean valid();
}
