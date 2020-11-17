package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.*;

import javax.security.auth.PrivateCredentialPermission;
import java.awt.*;

public class Client extends Proxy<IClient> implements org.powerbot.script.Client {

	public Client(final IClient wrapped) {
		super(wrapped, false);
	}

	public NodeDeque getProjectiles() {
		if (!isNull()) {
			return new NodeDeque(get().getProjectiles());
		}

		return null;
	}

	public boolean isMembers() {
		if (!isNull()) {
			return get().isMembers();
		}

		return false;
	}

	public int getCameraX() {
		if (!isNull()) {
			return get().getCameraX();
		}

		return -1;
	}

	public int getCameraY() {
		if (!isNull()) {
			return get().getCameraY();
		}

		return -1;
	}

	public int getCameraZ() {
		if (!isNull()) {
			return get().getCameraZ();
		}

		return -1;
	}

	public int getCameraYaw() {
		if (!isNull()) {
			return get().getCameraYaw();
		}

		return -1;
	}

	public int getCameraPitch() {
		if (!isNull()) {
			return get().getCameraPitch();
		}

		return -1;
	}

	public int getMinimapAngle() {
		if (!isNull()) {
			return get().getMinimapAngle();
		}

		return -1;
	}

	public Player getPlayer() {
		if (!isNull()) {
			return new Player(get().getPlayer());
		}

		return null;
	}

	public Player[] getPlayers() {
		if (!isNull()) {
			final IPlayer[] players = get().getPlayers();
			final Player[] wrapped = players != null ? new Player[players.length] : null;
			if (players != null) {
				for (int i = 0; i < players.length; i++) {
					wrapped[i] = new Player(players[i]);
				}
			}
			return wrapped;
		}

		return null;
	}

	public int[] getPlayerIndices() {
		if (!isNull()) {
			return get().getPlayerIndices();
		}

		return null;
	}

	public Npc[] getNpcs() {
		if (!isNull()) {
			final INpc[] npcs = get().getNpcs();
			final Npc[] wrapped = npcs != null ? new Npc[npcs.length] : null;
			if (npcs != null) {
				for (int i = 0; i < npcs.length; i++) {
					wrapped[i] = new Npc(npcs[i]);
				}
			}
			return wrapped;
		}

		return null;
	}

	public int[] getNpcIndices() {
		if (!isNull()) {
			return get().getNpcIndices();
		}

		return null;
	}

	public int getOffsetX() {
		if (!isNull()) {
			return get().getOffsetX();
		}

		return -1;
	}

	public int getOffsetY() {
		if (!isNull()) {
			return get().getOffsetY();
		}

		return -1;
	}

	public int getFloor() {
		if (!isNull()) {
			return get().getFloor();
		}

		return -1;
	}

	public Landscape getLandscape() {
		if (!isNull()) {
			return new Landscape(get().getLandscape());
		}

		return null;
	}

	public byte[][][] getLandscapeMeta() {
		if (!isNull()) {
			return get().getLandscapeMeta();
		}

		return null;
	}

	public int[][][] getTileHeights() {
		if (!isNull()) {
			return get().getTileHeights();
		}

		return null;
	}

	public boolean isMenuOpen() {
		if (!isNull()) {
			return get().isMenuOpen();
		}

		return false;
	}

	public int getMenuX() {
		if (!isNull()) {
			return get().getMenuX();
		}

		return -1;
	}

	public int getMenuY() {
		if (!isNull()) {
			return get().getMenuY();
		}

		return -1;
	}

	public int getMenuWidth() {
		if (!isNull()) {
			return get().getMenuWidth();
		}

		return -1;
	}

	public int getMenuHeight() {
		if (!isNull()) {
			return get().getMenuHeight();
		}

		return -1;
	}

	public int getMenuCount() {
		if (!isNull()) {
			return get().getMenuCount();
		}

		return -1;
	}

	public String[] getMenuActions() {
		if (!isNull()) {
			return get().getMenuActions();
		}

		return null;
	}

	public String[] getMenuOptions() {
		if (!isNull()) {
			return get().getMenuOptions();
		}

		return null;
	}

	public int[] getWidgetBoundsX() {
		if (!isNull()) {
			return get().getWidgetBoundsX();
		}

		return null;
	}

	public int[] getWidgetBoundsY() {
		if (!isNull()) {
			return get().getWidgetBoundsY();
		}

		return null;
	}

	public int[] getWidgetBoundsWidth() {
		if (!isNull()) {
			return get().getWidgetBoundsWidth();
		}

		return null;
	}

	public int[] getWidgetBoundsHeight() {
		if (!isNull()) {
			return get().getWidgetBoundsHeight();
		}

		return null;
	}

	public int getDestinationX() {
		if (!isNull()) {
			return get().getDestinationX();
		}

		return -1;
	}

	public int getDestinationY() {
		if (!isNull()) {
			return get().getDestinationY();
		}

		return -1;
	}

	public Widget[][] getWidgets() {
		if (!isNull()) {
			final IWidget[][] widgets = get().getWidgets();
			if (widgets == null) {
				return null;
			}
			final Widget[][] wrapped = new Widget[widgets.length][];
			for (int i = 0; i < widgets.length; i++) {
				final IWidget[] sub = widgets[i];
				if (sub == null) {
					wrapped[i] = null;
					continue;
				}

				final Widget[] sub2 = new Widget[sub.length];
				for (int i2 = 0; i2 < sub.length; i2++) {
					sub2[i2] = new Widget(sub[i2]);
				}
				wrapped[i] = sub2;
			}
			return wrapped;
		}

		return null;
	}

	public HashTable getWidgetTable() {
		if (!isNull()) {
			return new HashTable(get().getWidgetTable());
		}

		return null;
	}

	public NodeDeque[][][] getGroundItems() {
		if (!isNull()) {
			final INodeDeque[][][] items = get().getGroundItems();
			if (items == null) {
				return null;
			}

			final NodeDeque[][][] wrapped = new NodeDeque[items.length][][];
			for (int i = 0; i < items.length; i++) {
				final INodeDeque[][] sub = items[i];
				if (sub == null) {
					wrapped[i] = null;
					continue;
				}

				final NodeDeque[][] sub2 = new NodeDeque[sub.length][];
				wrapped[i] = sub2;
				for (int i2 = 0; i2 < sub.length; i2++) {
					final INodeDeque[] sub2_1 = sub[i2];
					if (sub2_1 == null) {
						sub2[i] = null;
						continue;
					}
					final NodeDeque[] sub2_2 = new NodeDeque[sub2_1.length];
					sub2[i2] = sub2_2;
					for (int i3 = 0; i3 < sub2_1.length; i3++) {
						sub2_2[i3] = new NodeDeque(sub2_1[i3]);
					}
				}
			}
			return wrapped;
		}

		return null;
	}

	public CollisionMap[] getCollisionMaps() {
		if (!isNull()) {
			final ICollisionMap[] maps = get().getCollisionMaps();
			final CollisionMap[] wrapped = maps != null ? new CollisionMap[maps.length] : null;
			if (maps != null) {
				for (int i = 0; i < maps.length; i++) {
					wrapped[i] = new CollisionMap(maps[i]);
				}
			}
			return wrapped;
		}

		return null;
	}

	public int[] getVarpbits() {
		if (!isNull()) {
			return get().getVarpbits();
		}

		return null;
	}

	public int getClientState() {
		if (!isNull()) {
			return get().getClientState();
		}

		return -1;
	}

	public int getCrosshairIndex() {
		if (!isNull()) {
			return get().getCrosshairIndex();
		}

		return -1;
	}

	public Cache getVarbitCache() {
		if (!isNull()) {
			return new Cache(get().getVarbitCache());
		}

		return null;
	}

	public Cache getNpcConfigCache() {
		if (!isNull()) {
			return new Cache(get().getNpcConfigCache());
		}

		return null;
	}

	public Cache getObjectConfigCache() {
		if (!isNull()) {
			return new Cache(get().getObjectConfigCache());
		}

		return null;
	}

	public Cache getItemConfigCache() {
		if (!isNull()) {
			return new Cache(get().getItemConfigCache());
		}

		return null;
	}

	public int[] getSkillLevels1() {
		if (!isNull()) {
			return get().getSkillLevels1();
		}

		return null;
	}

	public int[] getSkillLevels2() {
		if (!isNull()) {
			return get().getSkillLevels2();
		}

		return null;
	}

	public int[] getSkillExps() {
		if (!isNull()) {
			return get().getSkillExps();
		}

		return null;
	}

	public int getCycle() {
		if (!isNull()) {
			return get().getCycle();
		}

		return -1;
	}

	public int getHintArrowNpcUid() {
		if (!isNull()) {
			return get().getHintArrowNpcUid();
		}

		return -1;
	}

	public int getHintArrowPlayerUid() {
		if (!isNull()) {
			return get().getHintArrowPlayerUid();
		}

		return -1;
	}

	public int getHintArrowType() {
		if (!isNull()) {
			return get().getHintArrowType();
		}

		return -1;
	}

	public int getHintArrowX() {
		if (!isNull()) {
			return get().getHintArrowX();
		}

		return -1;
	}

	public int getHintArrowY() {
		if (!isNull()) {
			return get().getHintArrowY();
		}

		return -1;
	}

	public int getCurrentWorld() {
		if (!isNull()) {
			return get().getCurrentWorld();
		}

		return -1;
	}

	public int getSelectionType() {
		if (!isNull()) {
			return get().getSelectionType();
		}

		return -1;
	}

	public int getSelectionIndex() {
		if (!isNull()) {
			return get().getSelectionIndex();
		}

		return -1;
	}

	public String getUsername() {
		final SecurityManager s = System.getSecurityManager();
		if (s != null) {
			s.checkPermission(new PrivateCredentialPermission("rt4 u \"*\"", "read"));
		}

		if (!isNull()) {
			return get().getUsername();
		}

		return null;
	}

	public String getPassword() {
		final SecurityManager s = System.getSecurityManager();
		if (s != null) {
			s.checkPermission(new PrivateCredentialPermission("rt4 p \"*\"", "read"));
		}

		if (!isNull()) {
			return get().getPassword();
		}

		return null;
	}

	public int getPlayerIndex() {
		if (!isNull()) {
			return get().getPlayerIndex();
		}

		return -1;
	}

	public int getRunPercentage() {
		if (!isNull()) {
			return get().getRunPercentage();
		}

		return -1;
	}

	public EntryList getLoggerEntries() {
		if (!isNull()) {
			return new EntryList(get().getLoggerEntries());
		}

		return null;
	}

	public int getLoginState() {
		if (!isNull()) {
			return get().getLoginState();
		}

		return -1;
	}

	public int getLoginField() {
		if (!isNull()) {
			return get().getLoginField();
		}

		return -1;
	}

	public boolean isWorldSelectionUp() {
		if (!isNull()) {
			//TODO support this
			return false;
		}

		return false;
	}

	public int getTileSize() {
		if (!isNull()) {
			return get().getTileSize();
		}

		return -1;
	}

	public int getNpcCount() {
		if (!isNull()) {
			return get().getNpcCount();
		}

		return -1;
	}

	public int getPlayerCount() {
		if (!isNull()) {
			return get().getPlayerCount();
		}

		return -1;
	}

	public Cache getPlayerModelCache() {
		if (!isNull()) {
			return new Cache(get().getPlayerModelCache());
		}

		return null;
	}

	public Cache getObjectModelCache() {
		if (!isNull()) {
			return new Cache(get().getObjectModelCache());
		}

		return null;
	}

	public Cache getNpcModelCache() {
		if (!isNull()) {
			return new Cache(get().getNpcModelCache());
		}

		return null;
	}

	public Cache getWidgetModelCache() {
		if (!isNull()) {
			return new Cache(get().getWidgetModelCache());
		}

		return null;
	}

	public Cache getGroundItemModelCache() {
		if (!isNull()) {
			return new Cache(get().getGroundItemModelCache());
		}

		return null;
	}

	public Canvas getCanvas() {
		if (!isNull()) {
			return get().getCanvas();
		}

		return null;
	}
}
