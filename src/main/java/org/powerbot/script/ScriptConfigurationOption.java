package org.powerbot.script;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

public class ScriptConfigurationOption<T> {

	public enum OptionType {
		STRING, INTEGER, BOOLEAN,DOUBLE
	}

	private final String name;
	private final String description;
	private final OptionType optionType;
	private final T defaultValue;
	private T value;

	public ScriptConfigurationOption(final String name, final String description, final OptionType optionType, final T defaultValue) {
		this.name = name;
		this.description = description;
		this.optionType = optionType;
		this.defaultValue = defaultValue;
	}

	public static <T> ScriptConfigurationOption<T> fromAnnotation(final Script.ScriptConfiguration config) {
		T defaultValue;
		switch (config.optionType()) {
			case STRING:
				defaultValue = (T) config.defaultValue();
				break;
			case INTEGER:
				defaultValue = (T) Integer.valueOf(Integer.parseInt(config.defaultValue()));
				break;
			case BOOLEAN:
				defaultValue = (T) Boolean.valueOf(Boolean.parseBoolean(config.defaultValue()));
				break;
			case DOUBLE:
				defaultValue = (T) Double.valueOf(Double.parseDouble(config.defaultValue()));
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + config.optionType());
		}

		return new ScriptConfigurationOption<T>(config.name(), config.description(), config.optionType(), defaultValue);
	}

	public String getName() {
		return name;
	}

	public OptionType getOptionType() {
		return optionType;
	}

	public String getDescription() {
		return description;
	}

	public T getValue() {
		if (value == null) {
			return defaultValue;
		}

		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ScriptConfigurationOption<?> that = (ScriptConfigurationOption<?>) o;
		return Objects.equals(name, that.name) &&
			Objects.equals(description, that.description) &&
			optionType == that.optionType &&
			Objects.equals(defaultValue, that.defaultValue) &&
			Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, description, optionType, defaultValue, value);
	}
}
