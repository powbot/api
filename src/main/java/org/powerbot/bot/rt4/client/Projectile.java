package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IProjectile;

public class Projectile extends Proxy<IProjectile> {
	public Projectile(final IProjectile wrapped) {
		super(wrapped);
	}


	public int getId() {
		if (!isNull()) {
			return get().getId();
		}

		return -1;
	}

	public int getTargetIndex() {
		if (!isNull()) {
			return get().getTargetIndex();
		}

		return -1;
	}

	public int getStartX() {
		if (!isNull()) {
			return get().getStartX();
		}

		return -1;
	}

	public int getStartY() {
		if (!isNull()) {
			return get().getStartY();
		}

		return -1;
	}

	public int getPlane() {
		if (!isNull()) {
			return get().getPlane();
		}

		return -1;
	}

	public boolean isStarted() {
		if (!isNull()) {
			return get().isStarted();
		}

		return false;
	}

	public int getSlope() {
		if (!isNull()) {
			return get().getSlope();
		}

		return -1;
	}

	public int getEndHeight() {
		if (!isNull()) {
			return get().getEndHeight();
		}

		return -1;
	}

	public int getOrientation() {
		if (!isNull()) {
			return get().getOrientation();
		}

		return -1;
	}

	public int getStartDistance() {
		if (!isNull()) {
			return get().getStartDistance();
		}

		return -1;
	}

	public int getCycleStart() {
		if (!isNull()) {
			return get().getCycleStart();
		}

		return -1;
	}

	public double getX() {
		if (!isNull()) {
			return get().getX();
		}

		return -1D;
	}

	public double getY() {
		if (!isNull()) {
			return get().getY();
		}

		return -1D;
	}


}
