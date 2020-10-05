package org.powerbot.script.rt4;

import org.powerbot.bot.*;

import java.lang.reflect.Method;

/**
 * BasicObject
 * An object representing an internal game object.
 *
 * @see GameObject
 */
public class BasicObject {
	protected final org.powerbot.bot.rt4.client.BasicObject object;

	public BasicObject(final org.powerbot.bot.rt4.client.BasicObject object) {
		this.object = object;
	}

	boolean isComplex() {
		return getX() != -1;
	}

	public int getUid() {
		final long l = object.getUid();
		final int x = (int) l & 0x7f, z = (int) ((l >> 7) & 0x7f), i = (int) (l >> 17);
		return i << 14 | z << 7 | x;
	}

	public int getMeta() {
		return object.getMeta();
	}

	public int getX() {
		return object.getX();
	}

	public int getZ() {
		return object.getZ();
	}

	public int getX1() {
		return object.getX1();
	}

	public int getY1() {
		return object.getY1();
	}

	public int getX2() {
		return object.getX2();
	}

	public int getY2() {
		return object.getY2();
	}

	@Override
	public int hashCode() {
		return object.hashCode();
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof BasicObject && object.equals(((BasicObject) o).object);
	}
}
