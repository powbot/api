package org.powerbot.script;

import java.awt.*;

/**
 * Targetable
 * An entity which can be rendered in the viewport and has a closed shape.
 */
public interface Targetable {
	/**
	 * The next game screen point of the entity.
	 *
	 * @return the entity's next game screen point
	 */
	Point nextPoint();

	/**
	 * Whether or not the entity contains the point.
	 *
	 * @param point the point to test
	 * @return {@code true} is the entity contains the point; {@code false} otherwise.
	 */
	boolean contains(final Point point);
}
