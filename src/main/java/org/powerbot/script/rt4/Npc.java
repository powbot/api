package org.powerbot.script.rt4;

import org.powerbot.bot.rt4.HashTable;
import org.powerbot.bot.rt4.client.internal.IClient;
import org.powerbot.bot.rt4.client.internal.*;
import org.powerbot.bot.rt4.client.extended.IMobileClient;
import org.powerbot.script.*;
import org.powerbot.script.action.Emittable;
import org.powerbot.script.action.NpcAction;

import java.awt.*;

/**
 * Npc
 */
@Deprecated
public class Npc extends Actor implements Identifiable, Actionable, Nillable<Npc>, Emittable<NpcAction> {
	public static final Color TARGET_COLOR = new Color(255, 0, 255, 15);
	public static final int[] lookup;
	public static final Npc NIL = new Npc(org.powerbot.script.ClientContext.ctx(), null);

	static {
		lookup = new int[32];
		int i = 2;
		for (int j = 0; j < 32; j++) {
			lookup[j] = i - 1;
			i += i;
		}
	}

	private final INpc npc;
	private final int hash;

	public Npc(final ClientContext ctx, final INpc npc) {
		super(ctx);
		this.npc = npc;
		hash = System.identityHashCode(npc);
	}

	@Override
	protected IActor getActor() {
		return npc;
	}

	@Override
	public String name() {
		return npc != null ? npc.name() : "";
	}

	@Override
	public int combatLevel() {
		return npc != null ? npc.combatLevel() : -1;
	}

	@Override
	public int id() {
		return npc != null ? npc.id() : -1;
	}

	@Override
	public String[] actions() {
		return npc != null ? npc.actions() : new String[0];
	}

	@Override
	public boolean valid() {
		final IClient client = ctx.client();
		if (client == null || npc == null) {
			return false;
		}
		final INpc[] arr = client.getNpcs();
		for (final INpc a : arr) {
			if (npc.equals(a)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public String toString() {
		return String.format("%s[id=%d/name=%s/level=%d]",
			Npc.class.getName(), id(), name(), combatLevel());
	}

	public short[] colors1() {
		return npc != null ? npc.colors1() : new short[0];
	}

	public short[] colors2() {
		return npc != null ? npc.colors2() : new short[0];
	}

	@Override
	public int[] modelIds() {
		return npc != null ? npc.modelIds() : new int[0];
	}

	@Override
	public Npc nil() {
		return NIL;
  }
  
	public long getModelCacheId() {
		return npc != null ? npc.getModelCacheId() : -1;
	}

	@Override
	public ICache getModelCache() {
		return npc != null ? npc.getModelCache() : null;
	}

	@Override
	public NpcAction createAction(String action, boolean async) {
		return npc != null ? npc.createAction(action, async) : null;
	}
}
