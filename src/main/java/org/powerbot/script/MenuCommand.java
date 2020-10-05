package org.powerbot.script;

/**
 * MenuCommand
 * A component of the Menu containing an action and option.
 */
public class MenuCommand {
	public final String action, option;

	public MenuCommand(final String a, final String o) {
		action = a != null ? StringUtils.stripHtml(a) : "";
		option = o != null ? StringUtils.stripHtml(o) : "";
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = result * 31 + action.hashCode();
		result = result * 31 + option.hashCode();
		return result;
	}

	@Override
	public boolean equals(final Object o) {
		return o instanceof MenuCommand && action.equals(((MenuCommand) o).action) && option.equals(((MenuCommand) o).option);
	}

	@Override
	public String toString() {
		return String.format("%s %s", action, option).trim();
	}
}