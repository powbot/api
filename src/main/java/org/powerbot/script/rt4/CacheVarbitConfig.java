package org.powerbot.script.rt4;

import org.powerbot.bot.cache.AbstractCacheWorker;
import org.powerbot.script.Validatable;

/**
 * @author rvbiljouw
 */
public class CacheVarbitConfig implements Validatable {

	public interface Loader {

		CacheVarbitConfig byId(int id);

	}

	public static CacheVarbitConfig load(AbstractCacheWorker worker, int id) {
		return worker.varbitConfigLoader().byId(id);
	}

	public CacheVarbitConfig(int id) {
		this.id = id;
	}

	public CacheVarbitConfig(int id, int configId, int startBit, int endBit) {
		this.id = id;
		this.configId = configId;
		this.startBit = startBit;
		this.endBit = endBit;
	}

	public final int id;
	public int configId;
	public int startBit;
	public int endBit;

	@Override
	public boolean valid() {
		return id != -1;
	}

	@Override
	public String toString() {
		return "CacheVarbitConfig{" +
			"id=" + id +
			", configId=" + configId +
			", startBit=" + startBit +
			", endBit=" + endBit +
			'}';
	}
}
