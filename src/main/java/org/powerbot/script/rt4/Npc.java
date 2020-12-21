package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.HashTable;
import org.powerbot.bot.rt4.client.Cache;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.bot.rt4.client.NpcConfig;
import org.powerbot.bot.rt4.client.Varbit;
import org.powerbot.bot.rt4.client.internal.INode;
import org.powerbot.bot.rt4.client.internal.IVarbit;
import org.powerbot.script.Actionable;
import org.powerbot.script.Identifiable;
import org.powerbot.script.StringUtils;

import java.awt.*;

/**
 * Npc
 */
public class Npc extends Actor implements Identifiable, Actionable {
	public static final Color TARGET_COLOR = new Color(255, 0, 255, 15);
	private static final int[] lookup;

	static {
		lookup = new int[32];
		int i = 2;
		for (int j = 0; j < 32; j++) {
			lookup[j] = i - 1;
			i += i;
		}
	}

	private final org.powerbot.bot.rt4.client.Npc npc;
	private final int hash;

	Npc(final ClientContext ctx, final org.powerbot.bot.rt4.client.Npc npc) {
		super(ctx);
		this.npc = npc;
		hash = System.identityHashCode(npc);
	}

	@Override
	protected org.powerbot.bot.rt4.client.Actor getActor() {
		return npc;
	}

	@Override
	public String name() {
		final CacheNpcConfig c = CacheNpcConfig.load(ctx.bot().getCacheWorker(), id());
		return c != null ? StringUtils.stripHtml(c.name) : "";
	}

	@Override
	public int combatLevel() {
		final CacheNpcConfig c = CacheNpcConfig.load(ctx.bot().getCacheWorker(), id());
		return c != null ? c.level : -1;
	}

	@Override
	public int id() {
		final Client client = ctx.client();
		if (client == null) {
			return -1;
		}
		final NpcConfig c;
		if (npc == null) {
			c = new NpcConfig(null);
		} else {
			c = npc.getConfig();
		}
		if (c.isNull()) {
			return -1;
		}

		final int varbit = c.getVarbit(), si = c.getVarpbitIndex();
		int index = -1;
		if (varbit != -1) {
			final Cache cache = client.getVarbitCache();
			final HashTable<INode> table = new HashTable<>(cache.wrapped.get().getTable());
			final INode varbitNode = table.lookup(varbit);
			if (varbitNode instanceof IVarbit) {
				final Varbit varBit = new Varbit((IVarbit) varbitNode);
				final int mask = lookup[varBit.getEndBit() - varBit.getStartBit()];
				index = ctx.varpbits.varpbit(varBit.getIndex()) >> varBit.getStartBit() & mask;
			} else {
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
		final Client client = ctx.client();
		if (client == null || npc == null || npc.isNull()) {
			return false;
		}
		final org.powerbot.bot.rt4.client.Npc[] arr = client.getNpcs();
		for (final org.powerbot.bot.rt4.client.Npc a : arr) {
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

}
