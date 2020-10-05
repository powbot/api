package org.powerbot.script.rt4;

import org.powerbot.script.*;

/**
 * ChatOption
 */
public class ChatOption extends ClientAccessor implements Textable, Validatable {
	private final int index;
	private final Component option;

	public ChatOption(final ClientContext ctx, final int index, final Component option) {
		super(ctx);
		this.index = index;
		this.option = option;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public String text() {
		if (option == null) {
			return "";
		}
		return option.text();
	}

	public boolean select() {
		return select(false);
	}

	public boolean select(final boolean key) {
		return valid() && (key && ctx.input.send(Integer.toString(index + 1)) || option.click());
	}

	@Override
	public boolean valid() {
		return index >= 0 && index < 5 && option != null && option.valid();
	}
}
