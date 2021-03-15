package org.powerbot.bot.cache;

import org.powerbot.script.rt4.*;

public abstract class AbstractCacheWorker {

	public abstract Block getBlock(final int tree_index, final int block);

	public abstract CacheObjectConfig.Loader objectConfigLoader();

	public abstract CacheNpcConfig.Loader npcConfigLoader();

	public abstract CacheItemConfig.Loader itemConfigLoader();

	public abstract CacheVarbitConfig.Loader varbitConfigLoader();

	public abstract CacheModelConfig.Loader modelConfigLoader();

}
