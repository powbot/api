package org.powerbot.script.rt4;

import org.powerbot.script.*;

/**
 * @author Mooshe
 */
public class World extends ClientAccessor
		implements Nillable<World>, Validatable, Identifiable {

	public static final World NIL = new World(null, -1, Integer.MAX_VALUE,
			Type.UNKNOWN, Server.RUNE_SCAPE, Specialty.NONE);


	public enum Type {
		FREE(1130),
		MEMBERS(1131),
		DEAD_MAN(1238),
		PVP(1237),
		UNKNOWN(-1);

		public final int textureId;

		Type(final int id) {
			this.textureId = id;
		}

		public static Type forType(final int id) {
			for (final Type t : values()) {
				if (t.textureId == id) {
					return t;
				}
			}
			return Type.UNKNOWN;
		}
	}

	public enum Specialty {
		NONE,
		TRADE,
		MINI_GAME,
		PVP,
		TWISTED_LEAGUE,
		HIGH_RISK,
		NIGHTMARE_REWARD,
		BOUNTY_HUNTER,
		DEAD_MAN,
		SKILL_REQUIREMENT;

		public static Specialty get(final String str) {
		    if (str.contains("Trade")) {
			return Specialty.TRADE;
		    }
		    if (str.contains("PVP")) {
			return Specialty.PVP;
		    }
		    if (str.contains("Deadman")) {
			return Specialty.DEAD_MAN;
		    }
		    if (str.contains("Twisted")) {
			return Specialty.TWISTED_LEAGUE;
		    }
		    if (str.contains("Nightmare")) {
			return Specialty.NIGHTMARE_REWARD;
		    }
		    if (str.contains("High Risk")) {
			return Specialty.HIGH_RISK;
		    }
		    if (str.contains("Bounty")) {
			return Specialty.BOUNTY_HUNTER;
		    }
		    if (str.contains("skill t")) {
			return Specialty.SKILL_REQUIREMENT;
		    }
		    if (!str.contains("-")) {
			return Specialty.MINI_GAME;
		    }
		    return Specialty.NONE;
		}
	}

	public enum Server {
		RUNE_SCAPE(-1),
		NORTH_AMERICA(1133),
		AUSTRALIA(1137),
		GERMANY(1140),
		UNITED_KINGDOM(1135);

		public final int texture;

		Server(final int texture) {
			this.texture = texture;
		}

		public static Server forType(final int texture) {
			for (final Server s : values()) {
				if (s.texture == texture) {
					return s;
				}
			}
			return RUNE_SCAPE;
		}
	}

	private final int number, population, textColor;
	private final Type type;
	private final Server server;
	private final Specialty specialty;

	public World(final ClientContext ctx, final int number, final int population,
	             final Type type, final Server server, final Specialty specialty) {
		super(ctx);
		this.number = number;
		this.population = population;
		this.type = type;
		this.server = server;
		this.specialty = specialty;
		this.textColor = 0;
	}

	public World(final ClientContext ctx, final int number, final int population,
	             final Type type, final Server server, final Specialty specialty, final int textColor) {
		super(ctx);
		this.number = number;
		this.population = population;
		this.type = type;
		this.server = server;
		this.specialty = specialty;
		this.textColor = textColor;
	}

	/**
	 * Grabs the world number.
	 *
	 * @return The world number.
	 */
	@Override
	public int id() {
		return number;
	}

	/**
	 * The amount of players in the world.
	 *
	 * @return The player count.
	 */
	public int size() {
		return population;
	}

	/**
	 * The {@link Type} of world it is.
	 *
	 * @return The world type.
	 */
	public Type type() {
		return type;
	}

	/**
	 * The {@link Specialty} of the world.
	 *
	 * @return The world specialty.
	 */
	public Specialty specialty() {
		return specialty;
	}

	/**
	 * The {@link Server} the world is hosted on.
	 *
	 * @return The server location.
	 */
	public Server server() {
		return server;
	}

	/**
	 * Get the text colour of the world speciality, can be used to determine safe or joinable worlds.
	 *
	 * @return The text colour of the worlds Specialitity
	 */
	public int textColor() {
		return this.textColor;
	}

	/**
	 * Attempts to hop to this world.
	 *
	 * @return {@code true} if successfully hopped,
	 * {@code false} otherwise.
	 */
	public boolean hop() {
		if (!valid()) {
			return false;
		}
		ctx.worlds.open();
		final Component list = ctx.worlds.list();
		if (!list.valid()) {
			return false;
		}
		for (final Component c : list.components()) {
			if (c.index() % 6 != 2 || !c.text().equalsIgnoreCase("" + number)) {
				continue;
			}
			ctx.widgets.scroll(c, container(), bar(), true);
			if (c.click()) {
				if (!ctx.chat.pendingInput()) {
					if (!ctx.chat.continueChat("Switch")) {
						ctx.chat.continueChat("Yes.");
					}
				}
				return Condition.wait(new ClientStateCondition(45), 100, 20) &&
						Condition.wait(new ClientStateCondition(30), 100, 100);
			}
		}
		return false;
	}

	private Component component(final int widget, final int texture) {
		for (final Component c : ctx.widgets.widget(widget).components()) {
			if (c.textureId() == texture) {
				return c;
			}
		}
		return null;
	}

	private Component container() {
		final int height = ctx.worlds.list().height();
		return ctx.components.select(false, Worlds.WORLD_WIDGET).scrollHeight(height).poll();
	}

	private Component bar() {
		return ctx.components.select(false, Worlds.WORLD_WIDGET).select(c -> c.componentCount() == 6).poll();
	}

	@Override
	public World nil() {
		return NIL;
	}

	@Override
	public boolean valid() {
		return this.id() > -1;
	}

	@Override

	public String toString() {
		return "World[id=" + number + "/population=" + population + "/type=" + type + "/location=" +
				server + "/specialty=" + specialty + "]";
	}

	@Override
	public boolean equals(final Object o) {
		return (o instanceof World) && ((World) o).number == number;
	}

	private class ClientStateCondition extends Condition.Check {

		private final int state;

		private ClientStateCondition(final int state) {
			this.state = state;
		}


		@Override
		public boolean poll() {
			return ctx.game.clientState() == state;
		}
	}
}
