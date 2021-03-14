package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.client.internal.IWidget;
import org.powerbot.script.*;

/**
 * ChatOption
 */
public class ChatOption extends ClientAccessor implements Textable, Validatable, Nillable<ChatOption> {
	private final int index;
	private final IWidget option;

	public static final ChatOption NIL = new ChatOption(org.powerbot.script.ClientContext.ctx(), -1, Widgets.NIL);

	public ChatOption(final ClientContext ctx, final int index, final IWidget option) {
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

	@Override
	public ChatOption nil() {
		return ChatOption.NIL;
	}
}
