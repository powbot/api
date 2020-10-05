package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IProjectile;

public class Projectile extends Proxy<IProjectile> {
	public Projectile(final IProjectile wrapped) {
		super(wrapped);
	}


	public int getId() {
		if (!isNull()) {
			return wrapped.get().getId();
		}

		return -1;
	}

	public int getTargetIndex() {
		if (!isNull()) {
			return wrapped.get().getTargetIndex();
		}

		return -1;
	}

	public int getStartX() {
		if (!isNull()) {
			return wrapped.get().getStartX();
		}

		return -1;
	}

	public int getStartY() {
		if (!isNull()) {
			return wrapped.get().getStartY();
		}

		return -1;
	}

	public int getPlane() {
		if (!isNull()) {
			return wrapped.get().getPlane();
		}

		return -1;
	}

	public boolean isStarted() {
		if (!isNull()) {
			return wrapped.get().isStarted();
		}

		return false;
	}

	public int getSlope() {
		if (!isNull()) {
			return wrapped.get().getSlope();
		}

		return -1;
	}

	public int getEndHeight() {
		if (!isNull()) {
			return wrapped.get().getEndHeight();
		}

		return -1;
	}

	public int getOrientation() {
		if (!isNull()) {
			return wrapped.get().getOrientation();
		}

		return -1;
	}

	public int getStartDistance() {
		if (!isNull()) {
			return wrapped.get().getStartDistance();
		}

		return -1;
	}

	public int getCycleStart() {
		if (!isNull()) {
			return wrapped.get().getCycleStart();
		}

		return -1;
	}

	public double getX() {
		if (!isNull()) {
			return wrapped.get().getX();
		}

		return -1D;
	}

	public double getY() {
		if (!isNull()) {
			return wrapped.get().getY();
		}

		return -1D;
	}


}
