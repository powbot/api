package org.powerbot.script.rt4;

import org.powbot.stream.locatable.ProjectileStream;
import org.powbot.stream.Streamable;
import org.powerbot.bot.rt4.*;
import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.extended.IMobileClient;
import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.internal.IProjectile;

import java.util.ArrayList;
import java.util.List;

/**
 * Projectiles
 * {@link Projectiles} is a utility which provides access to the game's projectiles.
 * <p>
 * {@link org.powerbot.script.rt4.Projectile}s are game projectiles on the current plane which target an entity.
 */
public class Projectiles extends IdQuery<org.powerbot.script.rt4.Projectile> implements Streamable<ProjectileStream> {
	public Projectiles(final ClientContext factory) {
		super(factory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<org.powerbot.script.rt4.Projectile> get() {
		final List<org.powerbot.script.rt4.Projectile> items = new ArrayList<>();

		final IClient client = ctx.client();
		if (client == null) {
			return items;
		}

		if (client.isMobile()) {
			for (final IProjectile projectile : ((IMobileClient) client).getAllProjectiles()) {
				items.add(new org.powerbot.script.rt4.Projectile(ctx, projectile));
			}
		} else {
			for (final IProjectile n : NodeQueue.get(client.getProjectiles(), IProjectile.class)) {
				final org.powerbot.script.rt4.Projectile p = new org.powerbot.script.rt4.Projectile(ctx, n);
				items.add(p);
			}
		}

		return items;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.powerbot.script.rt4.Projectile nil() {
		return org.powerbot.script.rt4.Projectile.NIL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProjectileStream toStream() {
		return new ProjectileStream(ctx, get().stream());
	}
}
