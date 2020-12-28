package org.powerbot.script.action;

import org.powerbot.script.rt4.Npc;

public class NpcAction extends AbstractAction {
	private Npc npc;

	public Npc getNpc() {
		return npc;
	}

	public NpcAction setNpc(Npc npc) {
		this.npc = npc;
		return this;
	}
}
