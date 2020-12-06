package org.powerbot.util;

import org.powerbot.script.Script;
import org.powerbot.script.ScriptConfigurationOption;
import org.powerbot.script.StringUtils;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ScriptBundle {
	public final Definition definition;
	public final Class<? extends Script> script;
	public final AtomicReference<Script> instance;

	public ScriptBundle(final Definition definition, final Class<? extends Script> script) {
		this.definition = definition;
		this.script = script;
		instance = new AtomicReference<>(null);
	}

	public static final class Definition implements Comparable<Definition> {
		public static final String LOCALID = "0/local";
		private final String name, id, description;

		private List<ScriptConfigurationOption> configs;
		public String className;
		public byte[] key;
		public String source, website;
		public boolean local = false, assigned = false;
		public Type client;

		public Definition(final Script.Manifest manifest) {
			this(manifest, null);
		}

		public Definition(final Script.Manifest manifest, final String id) {
			this(manifest, new Script.ScriptConfiguration[0], id);
		}

		public Definition(final Script.Manifest manifest, final Script.ScriptConfiguration[] configs, final String id) {
			this(configs, manifest.name(), id, manifest.description());
		}

		Definition(final Script.ScriptConfiguration[] configs, final String name, final String id, final String description) {
			this.configs = Arrays.stream(configs).map(ScriptConfigurationOption::fromAnnotation).collect(Collectors.toList());
			this.name = name;
			this.id = id;
			this.description = description;
		}

		private String getCleanText(final String s) {
			return s == null || s.isEmpty() ? "" : StringUtils.stripHtml(s.trim());
		}

		public String getName() {
			return getCleanText(name);
		}

		public String getID() {
			return id == null ? getName() : id;
		}

		public String getDescription() {
			return getCleanText(description);
		}

		public List<ScriptConfigurationOption> getConfigs() {
			return configs;
		}

		@Override
		public String toString() {
			return getName().toLowerCase();
		}

		@Override
		public int compareTo(final Definition o) {
			if (o == null) {
				return 0;
			}

			final String a = getID(), b = o.getID();
			return a.compareTo(b);
		}
	}

	public static Map<String, String> parseProperties(final String s) {
		final Map<String, String> items = new HashMap<>();
		final String[] p = s.split("(?<!\\\\);");

		for (final String x : p) {
			final String[] e = x.split("(?<!\\\\)=", 2);
			items.put(e[0].trim(), e.length > 1 ? e[1].trim() : "");
		}

		return items;
	}

	public static void setClientMode(final Map<String, String> properties, final Definition def) {
		if (properties.containsKey("client")) {
			final String c = properties.get("client");
			if (c.equals("4")) {
				def.client = org.powerbot.script.rt4.ClientContext.class;
			}
		}
	}
}
