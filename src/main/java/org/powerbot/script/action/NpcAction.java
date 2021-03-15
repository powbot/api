package org.powerbot.script.action;

import org.powerbot.script.rt4.Npc;

import java.awt.*;

public class NpcAction extends AbstractAction<NpcAction> {
	private Npc npc;

	public Npc getNpc() {
		return npc;
	}

	public NpcAction setNpc(Npc npc) {
		this.npc = npc;

		if (getMouseX() == 0) {
			Point p = npc.nextPoint();
			if (p.x > 0) {
				setMouseX(p.x);
				setMouseY(p.y);
			}
		}
		return this;
	}
}
