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
import java.util.stream.Collectors;

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
		return getInternal().stream().map(p -> new Projectile(ctx, p)).collect(Collectors.toList());
	}

	public List<IProjectile> getInternal() {
		final List<IProjectile> items = new ArrayList<>();

		final IClient client = ctx.client();
		if (client == null) {
			return items;
		}

		if (client.isMobile()) {
			items.addAll(((IMobileClient) client).getAllProjectiles());
		} else {
			items.addAll(NodeQueue.get(client.getProjectiles(), IProjectile.class));
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
		return new ProjectileStream(ctx, getInternal().stream());
	}
}
