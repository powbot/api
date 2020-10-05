package org.powerbot.bot;

import org.powerbot.script.*;

public abstract class AbstractMouseSpline {

	public abstract int getPressDuration();

	public abstract Iterable<Vector3> getPath(final Vector3 a, final Vector3 b);

	public abstract long getAbsoluteDelay(final int z);
}
