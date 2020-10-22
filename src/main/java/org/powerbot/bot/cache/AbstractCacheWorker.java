package org.powerbot.bot.cache;

import org.powerbot.script.rt4.CacheItemConfig;
import org.powerbot.script.rt4.CacheNpcConfig;
import org.powerbot.script.rt4.CacheObjectConfig;
import org.powerbot.script.rt4.CacheVarbitConfig;

public abstract class AbstractCacheWorker {

	public abstract Block getBlock(final int tree_index, final int block);

	public abstract CacheObjectConfig.Loader objectConfigLoader();

	public abstract CacheNpcConfig.Loader npcConfigLoader();

	public abstract CacheItemConfig.Loader itemConfigLoader();

	public abstract CacheVarbitConfig.Loader varbitConfigLoader();

}
