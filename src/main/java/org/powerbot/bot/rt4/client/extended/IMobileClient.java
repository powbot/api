package org.powerbot.bot.rt4.client.extended;

import org.powerbot.bot.rt4.client.internal.IActor;
import org.powerbot.bot.rt4.client.internal.IProjectile;
import org.powerbot.script.Client;
import org.powerbot.script.rt4.GroundItem;

import java.awt.*;

public interface IMobileClient extends Client {

	@Override
	default boolean isMobile() {
		return false;
	}

	int getTileHeight(int x, int y, int z);

	int getLandscapeMeta(int x, int y, int z);

	Point worldToScreen(int x, int y, int z);

	int getNpcConfigIndex(int varBit);

	int getObjectConfigIndex(int varBit);

	int getWidgetParentId(int id);

	boolean isHealthBarVisible(IActor actor);

	int getHealthPercent(IActor actor);

	void setVarpbit(int index, int value);

	java.util.List<GroundItem> getAllGroundItems();

	java.util.List<IProjectile> getAllProjectiles();

}
