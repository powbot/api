package org.powerbot.bot.rt4.client.internal;

import org.powerbot.script.Actionable;
import org.powerbot.script.ClientContext;
import org.powerbot.script.Interactive;

import java.awt.*;
import java.util.concurrent.Callable;

public interface IItemNode extends IRenderable {

	int getItemId();

	int getStackSize();

}
