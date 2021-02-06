package org.powerbot.bot.rt4.client.extended;

import org.powerbot.bot.rt4.client.Projectile;
import org.powerbot.bot.rt4.client.internal.IActor;
import org.powerbot.bot.rt4.client.internal.IProjectile;
import org.powerbot.script.rt4.GroundItem;

import java.awt.*;

public interface IMobileClient {

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
