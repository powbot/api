package org.powerbot.bot;

import org.junit.Test;
import org.powerbot.script.*;
import org.powerbot.script.rt4.Constants;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class TestBase {

	static {
		System.setProperty("java.awt.headless", "true");
	}

	@Test
	public void environmentValid() {
		final Script.Manifest m = ContextClassLoader.class.getAnnotation(Script.Manifest.class);

		assertFalse(m.name().isEmpty());
		assertFalse(m.description().isEmpty());

		try {
			final InetAddress a = InetAddress.getByName(m.description());
		} catch (final UnknownHostException e) {
			fail();
		}
	}

	@Test
	public void constants() {
		assertEquals(Constants.GAME_LOGIN, 10);
	}
}
