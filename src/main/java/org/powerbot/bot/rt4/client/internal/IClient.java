package org.powerbot.bot.rt4.client.internal;

import org.powerbot.bot.EventDispatcher;

public interface IClient extends IGameEngine {

	int getCameraPitch();

	int getCameraX();

	int getCameraY();

	int getCameraYaw();

	int getCameraZ();

	int getClientState();

	ICollisionMap[] getCollisionMaps();

	int getCrosshairIndex();

	int getCycle();

	int getDestinationX();

	int getDestinationY();

	int getFloor();

	ICache getGroundItemModelCache();

	INodeDeque[][][] getGroundItems();

	int getHintArrowNpcUid();

	int getHintArrowPlayerUid();

	int getHintArrowType();

	int getHintArrowX();

	int getHintArrowY();

	ICache getItemConfigCache();

	ILandscape getLandscape();

	byte[][][] getLandscapeMeta();

	IEntryList getLoggerEntries();

	int getLoginField();

	String getLoginMessage1();

	String getLoginMessage2();

	String getLoginMessage3();

	int getLoginState();

	String[] getMenuActions();

	int getMenuCount();

	int getMenuHeight();

	String[] getMenuOptions();

	int getMenuWidth();

	int getMenuX();

	int getMenuY();

	int getMinimapAngle();

	ICache getNpcConfigCache();

	int getNpcCount();

	int[] getNpcIndices();

	ICache getNpcModelCache();

	INpc[] getNpcs();

	ICache getObjectConfigCache();

	ICache getObjectModelCache();

	int getOffsetX();

	int getOffsetY();

	String getPassword();

	IPlayer getPlayer();

	int getPlayerCount();

	int getPlayerIndex();

	int[] getPlayerIndices();

	ICache getPlayerModelCache();

	IPlayer[] getPlayers();

	INodeDeque getProjectiles();

	int getRunPercentage();

	int getSelectionIndex();

	int getSelectionType();

	int[] getSkillExps();

	int[] getSkillLevels1();

	int[] getSkillLevels2();

	int[][][] getTileHeights();

	int getTileSize();

	String getUsername();

	ICache getVarbitCache();

	int[] getVarpbits();

	int[] getWidgetBoundsHeight();

	int[] getWidgetBoundsWidth();

	int[] getWidgetBoundsX();

	int[] getWidgetBoundsY();

	ICache getWidgetModelCache();

	IHashTable getWidgetTable();

	IWidget[][] getWidgets();

	boolean isMembers();

	boolean isMenuOpen();

	void setEventDispatcher(EventDispatcher dispatcher);

}
