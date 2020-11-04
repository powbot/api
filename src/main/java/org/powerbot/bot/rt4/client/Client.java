package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.*;

import javax.security.auth.PrivateCredentialPermission;
import java.awt.*;

public class Client extends Proxy<IClient> implements org.powerbot.script.Client {

	public Client(final IClient wrapped) {
		super(wrapped);
	}

	public NodeDeque getProjectiles() {
		if (!isNull()) {
			return new NodeDeque(wrapped.get().getProjectiles());
		}

		return null;
	}

	public boolean isMembers() {
		if (!isNull()) {
			return wrapped.get().isMembers();
		}

		return false;
	}

	public int getCameraX() {
		if (!isNull()) {
			return wrapped.get().getCameraX();
		}

		return -1;
	}

	public int getCameraY() {
		if (!isNull()) {
			return wrapped.get().getCameraY();
		}

		return -1;
	}

	public int getCameraZ() {
		if (!isNull()) {
			return wrapped.get().getCameraZ();
		}

		return -1;
	}

	public int getCameraYaw() {
		if (!isNull()) {
			return wrapped.get().getCameraYaw();
		}

		return -1;
	}

	public int getCameraPitch() {
		if (!isNull()) {
			return wrapped.get().getCameraPitch();
		}

		return -1;
	}

	public int getMinimapAngle() {
		if (!isNull()) {
			return wrapped.get().getMinimapAngle();
		}

		return -1;
	}

	public Player getPlayer() {
		if (!isNull()) {
			return new Player(wrapped.get().getPlayer());
		}

		return null;
	}

	public Player[] getPlayers() {
		if (!isNull()) {
			final IPlayer[] players = wrapped.get().getPlayers();
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
			return wrapped.get().getPlayerIndices();
		}

		return null;
	}

	public Npc[] getNpcs() {
		if (!isNull()) {
			final INpc[] npcs = wrapped.get().getNpcs();
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
			return wrapped.get().getNpcIndices();
		}

		return null;
	}

	public int getOffsetX() {
		if (!isNull()) {
			return wrapped.get().getOffsetX();
		}

		return -1;
	}

	public int getOffsetY() {
		if (!isNull()) {
			return wrapped.get().getOffsetY();
		}

		return -1;
	}

	public int getFloor() {
		if (!isNull()) {
			return wrapped.get().getFloor();
		}

		return -1;
	}

	public Landscape getLandscape() {
		if (!isNull()) {
			return new Landscape(wrapped.get().getLandscape());
		}

		return null;
	}

	public byte[][][] getLandscapeMeta() {
		if (!isNull()) {
			return wrapped.get().getLandscapeMeta();
		}

		return null;
	}

	public int[][][] getTileHeights() {
		if (!isNull()) {
			return wrapped.get().getTileHeights();
		}

		return null;
	}

	public boolean isMenuOpen() {
		if (!isNull()) {
			return wrapped.get().isMenuOpen();
		}

		return false;
	}

	public int getMenuX() {
		if (!isNull()) {
			return wrapped.get().getMenuX();
		}

		return -1;
	}

	public int getMenuY() {
		if (!isNull()) {
			return wrapped.get().getMenuY();
		}

		return -1;
	}

	public int getMenuWidth() {
		if (!isNull()) {
			return wrapped.get().getMenuWidth();
		}

		return -1;
	}

	public int getMenuHeight() {
		if (!isNull()) {
			return wrapped.get().getMenuHeight();
		}

		return -1;
	}

	public int getMenuCount() {
		if (!isNull()) {
			return wrapped.get().getMenuCount();
		}

		return -1;
	}

	public String[] getMenuActions() {
		if (!isNull()) {
			return wrapped.get().getMenuActions();
		}

		return null;
	}

	public String[] getMenuOptions() {
		if (!isNull()) {
			return wrapped.get().getMenuOptions();
		}

		return null;
	}

	public int[] getWidgetBoundsX() {
		if (!isNull()) {
			return wrapped.get().getWidgetBoundsX();
		}

		return null;
	}

	public int[] getWidgetBoundsY() {
		if (!isNull()) {
			return wrapped.get().getWidgetBoundsY();
		}

		return null;
	}

	public int[] getWidgetBoundsWidth() {
		if (!isNull()) {
			return wrapped.get().getWidgetBoundsWidth();
		}

		return null;
	}

	public int[] getWidgetBoundsHeight() {
		if (!isNull()) {
			return wrapped.get().getWidgetBoundsHeight();
		}

		return null;
	}

	public int getDestinationX() {
		if (!isNull()) {
			return wrapped.get().getDestinationX();
		}

		return -1;
	}

	public int getDestinationY() {
		if (!isNull()) {
			return wrapped.get().getDestinationY();
		}

		return -1;
	}

	public Widget[][] getWidgets() {
		if (!isNull()) {
			final IWidget[][] widgets = wrapped.get().getWidgets();
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
			return new HashTable(wrapped.get().getWidgetTable());
		}

		return null;
	}

	public NodeDeque[][][] getGroundItems() {
		if (!isNull()) {
			final INodeDeque[][][] items = wrapped.get().getGroundItems();
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
			final ICollisionMap[] maps = wrapped.get().getCollisionMaps();
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
			return wrapped.get().getVarpbits();
		}

		return null;
	}

	public int getClientState() {
		if (!isNull()) {
			return wrapped.get().getClientState();
		}

		return -1;
	}

	public int getCrosshairIndex() {
		if (!isNull()) {
			return wrapped.get().getCrosshairIndex();
		}

		return -1;
	}

	public Cache getVarbitCache() {
		if (!isNull()) {
			return new Cache(wrapped.get().getVarbitCache());
		}

		return null;
	}

	public Cache getNpcConfigCache() {
		if (!isNull()) {
			return new Cache(wrapped.get().getNpcConfigCache());
		}

		return null;
	}

	public Cache getObjectConfigCache() {
		if (!isNull()) {
			return new Cache(wrapped.get().getObjectConfigCache());
		}

		return null;
	}

	public Cache getItemConfigCache() {
		if (!isNull()) {
			return new Cache(wrapped.get().getItemConfigCache());
		}

		return null;
	}

	public int[] getSkillLevels1() {
		if (!isNull()) {
			return wrapped.get().getSkillLevels1();
		}

		return null;
	}

	public int[] getSkillLevels2() {
		if (!isNull()) {
			return wrapped.get().getSkillLevels2();
		}

		return null;
	}

	public int[] getSkillExps() {
		if (!isNull()) {
			return wrapped.get().getSkillExps();
		}

		return null;
	}

	public int getCycle() {
		if (!isNull()) {
			return wrapped.get().getCycle();
		}

		return -1;
	}

	public int getHintArrowNpcUid() {
		if (!isNull()) {
			return wrapped.get().getHintArrowNpcUid();
		}

		return -1;
	}

	public int getHintArrowPlayerUid() {
		if (!isNull()) {
			return wrapped.get().getHintArrowPlayerUid();
		}

		return -1;
	}

	public int getHintArrowType() {
		if (!isNull()) {
			return wrapped.get().getHintArrowType();
		}

		return -1;
	}

	public int getHintArrowX() {
		if (!isNull()) {
			return wrapped.get().getHintArrowX();
		}

		return -1;
	}

	public int getHintArrowY() {
		if (!isNull()) {
			return wrapped.get().getHintArrowY();
		}

		return -1;
	}

	public int getCurrentWorld() {
		if (!isNull()) {
			return wrapped.get().getCurrentWorld();
		}

		return -1;
	}

	public int getSelectionType() {
		if (!isNull()) {
			return wrapped.get().getSelectionType();
		}

		return -1;
	}

	public int getSelectionIndex() {
		if (!isNull()) {
			return wrapped.get().getSelectionIndex();
		}

		return -1;
	}

	public String getUsername() {
		final SecurityManager s = System.getSecurityManager();
		if (s != null) {
			s.checkPermission(new PrivateCredentialPermission("rt4 u \"*\"", "read"));
		}

		if (!isNull()) {
			return wrapped.get().getUsername();
		}

		return null;
	}

	public String getPassword() {
		final SecurityManager s = System.getSecurityManager();
		if (s != null) {
			s.checkPermission(new PrivateCredentialPermission("rt4 p \"*\"", "read"));
		}

		if (!isNull()) {
			return wrapped.get().getPassword();
		}

		return null;
	}

	public int getPlayerIndex() {
		if (!isNull()) {
			return wrapped.get().getPlayerIndex();
		}

		return -1;
	}

	public int getRunPercentage() {
		if (!isNull()) {
			return wrapped.get().getRunPercentage();
		}

		return -1;
	}

	public EntryList getLoggerEntries() {
		if (!isNull()) {
			return new EntryList(wrapped.get().getLoggerEntries());
		}

		return null;
	}

	public int getLoginState() {
		if (!isNull()) {
			return wrapped.get().getLoginState();
		}

		return -1;
	}

	public int getLoginField() {
		if (!isNull()) {
			return wrapped.get().getLoginField();
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
			return wrapped.get().getTileSize();
		}

		return -1;
	}

	public int getNpcCount() {
		if (!isNull()) {
			return wrapped.get().getNpcCount();
		}

		return -1;
	}

	public int getPlayerCount() {
		if (!isNull()) {
			return wrapped.get().getPlayerCount();
		}

		return -1;
	}

	public Cache getPlayerModelCache() {
		if (!isNull()) {
			return new Cache(wrapped.get().getPlayerModelCache());
		}

		return null;
	}

	public Cache getObjectModelCache() {
		if (!isNull()) {
			return new Cache(wrapped.get().getObjectModelCache());
		}

		return null;
	}

	public Cache getNpcModelCache() {
		if (!isNull()) {
			return new Cache(wrapped.get().getNpcModelCache());
		}

		return null;
	}

	public Cache getWidgetModelCache() {
		if (!isNull()) {
			return new Cache(wrapped.get().getWidgetModelCache());
		}

		return null;
	}

	public Cache getGroundItemModelCache() {
		if (!isNull()) {
			return new Cache(wrapped.get().getGroundItemModelCache());
		}

		return null;
	}

	public Canvas getCanvas() {
		if (!isNull()) {
			return wrapped.get().getCanvas();
		}

		return null;
	}
}
