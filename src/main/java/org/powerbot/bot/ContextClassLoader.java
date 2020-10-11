package org.powerbot.bot;

import org.powerbot.script.*;

@Script.Manifest(name = "RSBot", description = "powbot.org")
public abstract class ContextClassLoader extends ClassLoader {
	private final ClientContext ctx;

	public ContextClassLoader(final ClientContext ctx, final ClassLoader parent) {
		super(parent);
		this.ctx = ctx;
	}

	public ClientContext ctx() {
		return ctx;
	}
}
