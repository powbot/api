package org.powerbot.bot;

public class FieldConfig {
	private final String getterName, parent, name, type;
	private final long multiplier;

	public FieldConfig(final String getterName, final String parent, final String name, final String type, final long multiplier) {
		this.getterName = getterName;
		this.parent = parent;
		this.name = name;
		this.type = type;
		this.multiplier = multiplier;
	}

	@Override
	public String toString() {
		return String.format("FieldConfig[getterName=%s;parent=%s;name=%s;type=%s;mult=%d;]", getterName, parent, name, type, multiplier);
	}

	public String getParent() {
		return parent;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public long getMultiplier() {
		return multiplier;
	}

	public String getGetterName() {
		return getterName;
	}
}
