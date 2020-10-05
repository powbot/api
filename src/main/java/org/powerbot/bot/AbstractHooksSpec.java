package org.powerbot.bot;

import java.util.Map;

public abstract class AbstractHooksSpec {

	public abstract Map<String, String> getInterfaces();

	public abstract Map<String, FieldConfig> getFieldConfigs();
}
