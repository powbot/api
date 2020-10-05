package org.powerbot.script;

/**
 * Nillable
 * An object representing "nothing" which avoids nasty {@link NullPointerException} that have to be manually handled when using null directly.
 *
 * @param <E> the type of object to represent null
 */
public interface Nillable<E> {
	/**
	 * A "null" instance. An object with no function. All methods will not evaluate to anything.
	 *
	 * @return "nothing"
	 */
	E nil();
}
