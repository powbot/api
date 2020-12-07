package org.powerbot.script;

import java.util.Arrays;
import java.util.Objects;

public class ScriptConfigurationOption<T> {

	public enum OptionType {
		STRING, INTEGER, BOOLEAN,DOUBLE
	}

	private final String name;
	private final String description;
	private final OptionType optionType;
	private final T defaultValue;
	private boolean isConfigured;
	private final String[] allowedValues;
	private T value;

	public ScriptConfigurationOption(final String name, final String description, final OptionType optionType, final T defaultValue, String[] allowedValues) {
		this.name = name;
		this.description = description;
		this.optionType = optionType;
		this.defaultValue = defaultValue;
		this.allowedValues = allowedValues;
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

		return new ScriptConfigurationOption<T>(config.name(), config.description(), config.optionType(), defaultValue, config.allowedValues());
	}

	/**
	 * The name of the configuration option, should be unique per script
	 * @return string
	 */
	public String getName() {
		return name;
	}

	/**
	 * The data type of the configuration option
	 * @return OptionType
	 */
	public OptionType getOptionType() {
		return optionType;
	}

	/**
	 * A description of the configuration to present to the end user configuring it
	 * @return string
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * The value provided by the end user for this configuration or the default value if the user provided no value then the default value is used
	 * @return value - data type depends on #getOptionType()
	 */
	public T getValue() {
		if (value == null) {
			return defaultValue;
		}

		return value;
	}

	/**
	 * Check whether or not the user provided a value for this configuration
	 * @return true if the user provided no value and the default value is being used
	 */
	public boolean isConfigured() {
		return isConfigured;
	}

	/**
	 * Get the allowed values of this configuration
	 * @return array
	 */
	public String[] getAllowedValues() {
		return allowedValues;
	}

	public void setValue(T value) {
		this.isConfigured = true;

		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ScriptConfigurationOption<?> that = (ScriptConfigurationOption<?>) o;
		return isConfigured == that.isConfigured &&
			Objects.equals(name, that.name) &&
			Objects.equals(description, that.description) &&
			optionType == that.optionType &&
			Objects.equals(defaultValue, that.defaultValue) &&
			Arrays.equals(allowedValues, that.allowedValues) &&
			Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(name, description, optionType, defaultValue, isConfigured, value);
		result = 31 * result + Arrays.hashCode(allowedValues);
		return result;
	}
}
