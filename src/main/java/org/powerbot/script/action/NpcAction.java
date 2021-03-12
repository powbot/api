package org.powerbot.script.action;

import org.powerbot.script.rt4.Npc;

import java.awt.*;

public class NpcAction extends AbstractAction<NpcAction> {
	private int slot;
	private int interactionIndex;

	public int getInteractionIndex() {
		return interactionIndex;
	}

	public NpcAction setInteractionIndex(int interactionIndex) {
		this.interactionIndex = interactionIndex;
		return this;
	}

	public int getNpcIndex() {
		return slot;
	}

	public NpcAction setNpcIndex(int slot) {
		this.slot = slot;
		return this;
	}
}
