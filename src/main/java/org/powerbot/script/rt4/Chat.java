package org.powerbot.script.rt4;

import org.powbot.stream.widget.ChatOptionStream;
import org.powbot.stream.Streamable;
import org.powerbot.script.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Chat
 * A utility class for simplifying interacting with the chat box.
 */
public class Chat extends TextQuery<ChatOption> implements Streamable<ChatOptionStream> {
	private final AtomicBoolean registered = new AtomicBoolean(false);

	public Chat(final ClientContext ctx) {
		super(ctx);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ChatOption> get() {
		final List<ChatOption> options = new ArrayList<>(5);
		final Component parent = ctx.widgets.component(Constants.CHAT_WIDGET, 1);
		for (int i = 0; i < 5; i++) {
			final Component component = parent.component(Constants.CHAT_OPTIONS[i]);
			if (!component.valid() || component.textureId() != -1) {
				continue;
			}
			options.add(new ChatOption(ctx, i, component));
		}
		return options;
	}

	public boolean chatting() {
		if (ctx.widgets.component(Constants.CHAT_WIDGET, 0).valid()) {
			return true;
		}
		for (final int[] arr : Constants.CHAT_CONTINUES) {
			if (ctx.widgets.component(arr[0], 0).valid()) {
				return true;
			}
		}

		return false;
	}


	/**
	 * Determines if the chat is continuable.
	 *
	 * @return {@code true} if the chat is continuable; otherwise {@code false}
	 */
	public boolean canContinue() {
		return getContinue() != null;
	}

	@Deprecated
	public List<Component> chatOptions() {
		final List<Component> options = new ArrayList<>();
		final Component component = ctx.widgets.component(Constants.CHAT_WIDGET, 0);
		for (int i = 1; i < component.componentCount() - 2; i++) {
			options.add(component.components()[i]);
		}
		return options;
	}

	public boolean continueChat(final String... options) {
		return continueChat(false, options);
	}

	public boolean continueChat(final boolean useKeys, final String... options) {
		if (!chatting()) {
			return false;
		}
		if (canContinue()) {
			return clickContinue(useKeys);
		}
		if (options != null) {
			final ChatOption option = ctx.chat.select().text(options).peek();
			if (option.valid()) {
				return option.select(useKeys);
			}
		}
		return false;
	}

	private Component getContinue() {
		for (final int[] a : Constants.CHAT_CONTINUES) {
			final Component c = ctx.components.select(false, a[0]).textContains("Click here to continue").poll();
			if (!c.valid()) {
				continue;
			}
			return c;
		}
		return null;
	}

	/**
	 * Continues the chat.
	 *
	 * @return {@code true} if the chat was continued; otherwise {@code false}
	 */
	public boolean clickContinue() {
		return clickContinue(false);
	}

	/**
	 * Continues the chat.
	 *
	 * @param key {@code true} to press space; {@code false} to use the mouse.
	 * @return {@code true} if the chat was continued; otherwise {@code false}
	 */
	public boolean clickContinue(final boolean key) {
		final Component c = getContinue();
		return c != null && (key && ctx.input.send(" ") || c.click());
	}


	public boolean pendingInput() {
		return inputBox().visible();
	}

	public boolean sendInput(final int input) {
		return sendInput(Integer.toString(input));
	}

	public boolean sendInput(final String input) {
		final Component textBox = inputBox();
		if (!pendingInput()) {
			return false;
		}

		String text = textBox.text().replace("*", "");
		if (text.equalsIgnoreCase(input)) {
			return ctx.input.sendln("");
		}

		for (int i = 0; i <= text.length(); ++i) {
			ctx.input.send("{VK_BACK_SPACE down}");
			Condition.sleep(60);
			ctx.input.send("{VK_BACK_SPACE up}");
			Condition.sleep(60);
		}

		ctx.input.send(input);
		text = textBox.text().replace("*", "");
		return text.equalsIgnoreCase(input) && textBox.visible() && ctx.input.sendln("");
	}

	private Component inputBox() {
		return ctx.widgets.component(Constants.CHAT_INPUT, Constants.CHAT_INPUT_TEXT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChatOption nil() {
		return new ChatOption(ctx, -1, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChatOptionStream toStream() {
		return new ChatOptionStream(ctx, get().stream());
	}
}
