package org.powerbot.bot.rt4.client.internal;

import java.util.LinkedHashMap;

/**
 * @author rvbiljouw
 */
public interface IPreferences {

	boolean getRoofsHidden();

	void setRoofsHidden(boolean roofsHidden);

	boolean getLoginMusicDisabled();

	void setLoginMusicDisabled(boolean disabled);

	boolean getUnknownFlag2();

	void setUnknownFlag2(boolean b);

	String getRememberedLogin();

	void setRememberedLogin(String login);

	int getDisplayMode();

	void setDisplayMode(int displayMode);

	LinkedHashMap getParameters();

	void setParameters(LinkedHashMap parameters);

}
