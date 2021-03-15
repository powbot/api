package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.HashTable;
import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.internal.*;
import org.powerbot.bot.rt4.client.extended.IMobileClient;
import org.powerbot.script.*;
import org.powerbot.script.action.Emittable;
import org.powerbot.script.action.NpcAction;

import java.awt.*;

/**
 * Npc
 */
public class Npc extends Actor implements Identifiable, Actionable, Nillable<Npc>, Emittable<NpcAction> {
	public static final Color TARGET_COLOR = new Color(255, 0, 255, 15);
	private static final int[] lookup;
	public static final Npc NIL = new Npc(org.powerbot.script.ClientContext.ctx(), null);

	static {
		lookup = new int[32];
		int i = 2;
		for (int j = 0; j < 32; j++) {
			lookup[j] = i - 1;
			i += i;
		}
	}

	private final INpc npc;
	private final int hash;

	Npc(final ClientContext ctx, final INpc npc) {
		super(ctx);
		this.npc = npc;
		hash = System.identityHashCode(npc);
	}

	@Override
	protected IActor getActor() {
		return npc;
	}

	@Override
	public String name() {
		final String raw = rawName();
		return raw != null ? StringUtils.stripHtml(raw) : "";
	}

	public String rawName() {
		if (npc != null && npc.getConfig() != null && npc.getConfig().getName() != null) {
			return npc.getConfig().getName();
		}

		final CacheNpcConfig c = CacheNpcConfig.load(ctx.bot().getCacheWorker(), id());
		return c != null ? c.name : "";
	}

	@Override
	public int combatLevel() {
		final CacheNpcConfig c = CacheNpcConfig.load(ctx.bot().getCacheWorker(), id());
		return c != null ? c.level : -1;
	}

	@Override
	public int id() {
		final IClient client = ctx.client();
		if (client == null) {
			return -1;
		}
		final INpcConfig c = npc != null ? npc.getConfig() : null;
		if (c == null) {
			return -1;
		}

		final int varbit = c.getVarbit(), si = c.getVarpbitIndex();
		int index = -1;
		if (varbit != -1) {
			if (ctx.client().isMobile()) {
				index = ((IMobileClient) ctx.client()).getNpcConfigIndex(varbit);
			} else {
				final ICache cache = client.getVarbitCache();
				final HashTable<INode> table = new HashTable<>(cache.getTable());
				final INode varbitNode = table.lookup(varbit);
				if (varbitNode instanceof IVarbit) {
					final IVarbit varBit = (IVarbit) varbitNode;
					final int mask = lookup[varBit.getEndBit() - varBit.getStartBit()];
					index = ctx.varpbits.varpbit(varBit.getIndex()) >> varBit.getStartBit() & mask;
				}
			}

			if (index == -1) {
				final CacheVarbitConfig cachedVarbit = CacheVarbitConfig.load(ctx.bot().getCacheWorker(), varbit);
				final int mask = lookup[cachedVarbit.endBit - cachedVarbit.startBit];
				index = ctx.varpbits.varpbit(cachedVarbit.configId) >> cachedVarbit.startBit & mask;
			}
		} else if (si != -1) {
			index = ctx.varpbits.varpbit(si);
		}

		if (index >= 0) {
			final int[] configs = c.getConfigs();
			if (index < configs.length && configs[index] != -1) {
				return configs[index];
			}
		}
		return c.getId();
	}

	@Override
	public String[] actions() {
		final CacheNpcConfig c = CacheNpcConfig.load(ctx.bot().getCacheWorker(), id());
		return c != null ? c.actions : new String[0];
	}

	@Override
	public boolean valid() {
		final IClient client = ctx.client();
		if (client == null || npc == null) {
			return false;
		}
		final INpc[] arr = client.getNpcs();
		for (final INpc a : arr) {
			if (npc.equals(a)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public String toString() {
		return String.format("%s[id=%d/name=%s/level=%d]",
			Npc.class.getName(), id(), name(), combatLevel());
	}

	public short[] colors1() {
		final CacheNpcConfig c = CacheNpcConfig.load(ctx.bot().getCacheWorker(), id());
		return c != null ? c.recolorOriginal : new short[]{};
	}

	public short[] colors2() {
		final CacheNpcConfig c = CacheNpcConfig.load(ctx.bot().getCacheWorker(), id());
		return c != null ? c.recolorTarget : new short[]{};
	}

	@Override
	public int[] modelIds() {
		final CacheNpcConfig c = CacheNpcConfig.load(ctx.bot().getCacheWorker(), id());

		return c != null ? c.modelIds : null;
	}

	@Override
	public Npc nil() {
		return NIL;
  }
  
	public long getModelCacheId() {
		return id();
	}

	@Override
	public ICache getModelCache() {
		final IClient client = ctx.client();
		if (client == null) {
			return null;
		}

		return client.getNpcModelCache();
	}

	@Override
	public NpcAction createAction(String action) {
		return createAction(action, false);
	}

	@Override
	public NpcAction createAction(String action, boolean async) {
		return new NpcAction().setNpc(this).setInteraction(action).setAsync(async);
	}
}
