package org.powerbot.script.rt4;

import org.powerbot.bot.inventory.InventoryWatcher;
import org.powerbot.bot.model.ModelCache;
import org.powerbot.bot.rt4.*;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.*;

import java.util.List;

/**
 * ClientContext
 * A utility class with references to all major points of the API.
 */
public class ClientContext extends org.powerbot.script.ClientContext<Client> {
	public final Bank bank;
	public final Camera camera;
	public final Chat chat;
	public final Combat combat;
	public final DepositBox depositBox;
	public final Equipment equipment;
	public final Game game;
	public final GroundItems groundItems;
	public final Inventory inventory;
	public final Magic magic;
	public final Menu menu;
	public final Movement movement;
	public final Npcs npcs;
	public final Objects objects;
	public final Players players;
	public final Prayer prayer;
	public final Skills skills;
	public final Varpbits varpbits;
	public final Widgets widgets;
	public final Worlds worlds;
	public final Projectiles projectiles;
	public final Components components;
	public static final ModelCache modelCache = new ModelCache();
	public static final InventoryWatcher inventoryWatcher = new InventoryWatcher();


	private ClientContext(final Bot<ClientContext> bot) {
		super(bot);
		bank = new Bank(this);
		camera = new Camera(this);
		chat = new Chat(this);
		combat = new Combat(this);
		depositBox = new DepositBox(this);
		equipment = new Equipment(this);
		game = new Game(this);
		groundItems = new GroundItems(this);
		inventory = new Inventory(this);
		magic = new Magic(this);
		menu = new Menu(this);
		movement = new Movement(this);
		npcs = new Npcs(this);
		objects = new Objects(this);
		players = new Players(this);
		prayer = new Prayer(this);
		skills = new Skills(this);
		varpbits = new Varpbits(this);
		widgets = new Widgets(this);
		worlds = new Worlds(this);
		projectiles = new Projectiles(this);
		components = new Components(this);
	}

	/**
	 * Creates a new chained context.
	 *
	 * @param ctx the parent context
	 */
	public ClientContext(final ClientContext ctx) {
		super(ctx);

		bank = ctx.bank;
		camera = ctx.camera;
		chat = ctx.chat;
		combat = ctx.combat;
		depositBox = ctx.depositBox;
		equipment = ctx.equipment;
		game = ctx.game;
		groundItems = ctx.groundItems;
		inventory = ctx.inventory;
		magic = ctx.magic;
		menu = ctx.menu;
		movement = ctx.movement;
		npcs = ctx.npcs;
		objects = ctx.objects;
		players = ctx.players;
		prayer = ctx.prayer;
		skills = ctx.skills;
		varpbits = ctx.varpbits;
		widgets = ctx.widgets;
		worlds = ctx.worlds;
		projectiles = ctx.projectiles;
		components = ctx.components;
	}

	/**
	 * Creates a new context for the given {@link org.powerbot.script.Bot}.
	 *
	 * @param bot the bot to associate with
	 * @return a new context
	 */
	public static ClientContext newContext(final Bot<ClientContext> bot) {
		return new ClientContext(bot);
	}
}
