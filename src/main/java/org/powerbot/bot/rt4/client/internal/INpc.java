package org.powerbot.bot.rt4.client.internal;

import org.powerbot.bot.rt4.HashTable;
import org.powerbot.bot.rt4.client.extended.IMobileClient;
import org.powerbot.script.ClientContext;
import org.powerbot.script.Nillable;
import org.powerbot.script.StringUtils;
import org.powerbot.script.rt4.CacheNpcConfig;
import org.powerbot.script.rt4.CacheVarbitConfig;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Npcs;

import java.awt.*;
import java.util.concurrent.Callable;

public interface INpc extends IActor, Nillable<INpc> {

	INpcConfig getConfig();

	default int combatLevel() {
		final CacheNpcConfig c = CacheNpcConfig.load(ctx().bot().getCacheWorker(), id());
		return c != null ? c.level : -1;
	}

	default short[] colors1() {
		final CacheNpcConfig c = CacheNpcConfig.load(ctx().bot().getCacheWorker(), id());
		return c != null ? c.recolorOriginal : new short[0];
	}

	default short[] colors2() {
		final CacheNpcConfig c = CacheNpcConfig.load(ctx().bot().getCacheWorker(), id());
		return c != null ? c.recolorTarget : new short[0];
	}

	@Override
	default int id() {
		final org.powerbot.script.rt4.ClientContext ctx = ctx();
		final IClient client = ctx.client();
		if (client == null) {
			return -1;
		}
		final INpcConfig c = getConfig();
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
					final int mask = Npc.lookup[varBit.getEndBit() - varBit.getStartBit()];
					index = ctx.varpbits.varpbit(varBit.getIndex()) >> varBit.getStartBit() & mask;
				}
			}

			if (index == -1) {
				final CacheVarbitConfig cachedVarbit = CacheVarbitConfig.load(ctx.bot().getCacheWorker(), varbit);
				final int mask = Npc.lookup[cachedVarbit.endBit - cachedVarbit.startBit];
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
	default int[] modelIds() {
		final CacheNpcConfig c = CacheNpcConfig.load(ctx().bot().getCacheWorker(), id());

		return c != null ? c.modelIds : null;
	}

	@Override
	default String name() {
		final String raw = rawName();
		return raw != null ? StringUtils.stripHtml(raw) : "";
	}

	default String rawName() {
		if (getConfig() != null && getConfig().getName() != null) {
			return getConfig().getName();
		}

		final CacheNpcConfig c = CacheNpcConfig.load(ctx().bot().getCacheWorker(), id());
		return c != null ? c.name : "";
	}

	@Override
	default String[] actions() {
		final CacheNpcConfig c = CacheNpcConfig.load(ctx().bot().getCacheWorker(), id());
		return c != null ? c.actions : new String[0];
	}

	@Override
	default INpc nil() {
		return Npcs.NIL;
	}
}
