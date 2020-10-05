package org.powerbot.script.rt4;

import org.powerbot.script.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to manipulate the world switcher interface.
 */
public class Worlds extends AbstractQuery<Worlds, World, ClientContext> implements Identifiable.Query<Worlds> {
	public static final int WORLD_WIDGET = 69, LOGOUT_WIDGET = 182;
	private ArrayList<World> cache = new ArrayList<>();

	/**
	 * A query of worlds which could be hopped to.
	 *
	 * @param ctx The client context.
	 */
	public Worlds(final ClientContext ctx) {
		super(ctx);
	}

	@Override
	protected Worlds getThis() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<World> get() {
		final ArrayList<World> worlds = new ArrayList<>();
		final Component list = list();
		if (!list.valid()) {
			return cache;
		}
		final Component[] comps = list.components();
		for (int off = 0; off < comps.length - 6; off += 6) {
			final World.Type type = World.Type.forType(comps[off + 1].textureId());
			final World.Server server = World.Server.forType(comps[off + 3].textureId());
			final World.Specialty special = World.Specialty.get(comps[off + 5].text());
			final int number = Integer.parseInt(comps[off + 2].text());
			final int population = Integer.parseInt(comps[off + 4].text());
			final int textColour = comps[off + 5].textColor();
			worlds.add(new World(ctx, number, population, type, server, special, textColour));
		}
		cache = new ArrayList<>(worlds);
		return worlds;
	}

	/**
	 * Filters the worlds by types.
	 *
	 * @param types The types to target.
	 * @return this instance for chaining purposes.
	 */
	public Worlds types(final World.Type... types) {
		return select(world -> {
			for (final World.Type t : types) {
				if (t.equals(world.type())) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Filters the worlds by specialties.
	 *
	 * @param specialties The specialties to target.
	 * @return this instance for chaining purposes.
	 */
	public Worlds specialties(final World.Specialty... specialties) {
		return select(world -> {
			for (final World.Specialty s : specialties) {
				if (s.equals(world.specialty())) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Filters the worlds down to the specified servers.
	 *
	 * @param servers The server locations to filter.
	 * @return This instance for chaining purposes.
	 */
	public Worlds servers(final World.Server... servers) {
		return select(world -> {
			for (final World.Server s : servers) {
				if (s.equals(world.server())) {
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Filters the worlds by player count. This will filter down to any world
	 * which is less than or equal to the parameter.
	 *
	 * @param population The population the worlds should be less than or equal to.
	 * @return this instance for chaining purposes.
	 */
	public Worlds population(final int population) {
		return select(world -> world.size() <= population);
	}

	/**
	 * Filters the worlds by joinable worlds. This will filter out any
	 * dangerous or skill-required worlds* [*where the requirement is not held].
	 *
	 * @return this instance for chaining purposes.
	 */
	public Worlds joinable() {
		return select(world -> world.valid() &&
				world.type() != World.Type.DEAD_MAN &&
				world.specialty() != World.Specialty.PVP &&
				world.textColor() != 8355711);
	}

	/**
	 * Opens the world switcher.
	 *
	 * @return {@code true} if successfully opened, {@code false} otherwise.
	 */
	public boolean open() {
		if (ctx.widgets.widget(WORLD_WIDGET).valid()) {
			return true;
		}
		ctx.game.tab(Game.Tab.LOGOUT);
		final Component c = component(LOGOUT_WIDGET, "World Switcher");
		return c.valid() && c.click() && Condition.wait(new Condition.Check() {
			public boolean poll() {
				return ctx.widgets.widget(WORLD_WIDGET).valid();
			}
		}, 100, 20);
	}

	@Override
	public World nil() {
		return World.NIL;
	}

	protected final Component list() {
		return ctx.components.select(false, WORLD_WIDGET).select(c -> c.componentCount() > 800).width(174).poll();
	}

	protected final Component component(final int widget, final String text) {
		return ctx.components.select(widget).select(c -> c.text().equalsIgnoreCase(text)).poll();
	}

	@Override
	public Worlds id(final int... ids) {
		return select(new Identifiable.Matcher(ids));
	}
}
