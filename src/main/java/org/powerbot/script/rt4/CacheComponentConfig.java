package org.powerbot.script.rt4;

import org.powerbot.bot.cache.AbstractCacheWorker;
import org.powerbot.bot.cache.Block;
import org.powerbot.bot.cache.JagexBufferStream;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheComponentConfig {

	private static final Map<Integer, CacheComponentConfig[]> CACHE = new ConcurrentHashMap<>();

	private static final int CACHE_IDX = 3;

	public int componentId;
	public int id;
	public int type;
	public int contentType;
	public int originalX;
	public int originalY;
	public int originalWidth;
	public int originalHeight;
	public int widthMode;
	public int heightMode;
	public int xPositionMode;
	public int yPositionMode;
	public int parentId = -1;
	public boolean isHidden;
	public int scrollWidth;
	public int scrollHeight;
	public boolean noClickThrough;
	public int spriteId = -1;
	public int textureId;
	public boolean spriteTiling;
	public int opacity;
	public int borderType;
	public int shadowColor;
	public boolean flippedY;
	public boolean flippedX;
	public int modelType = 1;
	public int modelId = -1;
	public int offsetX2d;
	public int offsetY2d;
	public int rotationX;
	public int rotationY;
	public int rotationZ;
	public int modelZoom = 100;
	public int animation = -1;
	public boolean orthogonal;
	public int modelHeightOverride;
	public int fontId = -1;
	public String text = "";
	public int lineHeight;
	public int xTextAlignment;
	public int yTextAlignment;
	public int textColor;
	public boolean textShadowed;
	public boolean filled;
	public int lineWidth = 1;
	public boolean lineDirection;
	public int clickMask;
	public String name = "";
	public String[] actions;
	public int dragDeadZone;
	public int dragDeadTime;
	public boolean dragRenderBehavior;
	public String targetVerb = "";
	public int menuType;
	public int hoveredSiblingId;
	public int[] alternateOperators;
	public int[] alternateRhs;
	public int[][] x;
	public int[] itemIds;
	public int[] itemQuantities;
	public int xPitch;
	public int yPitch;
	public int[] spriteXOffsets;
	public int[] spriteYOffsets;
	public int[] sprites;
	public String[] configActions;
	public String alternateText = "";
	public int alternateTextColor;
	public int mouseOverTextColor;
	public int mouseOverTextColor2;
	public int alternateSpriteId = -1;
	public int alternateModelId = -1;
	public int alternateAnimation = -1;
	public String spellName = "";
	public String tooltip = "Ok";
	private final JagexBufferStream stream;

	private CacheComponentConfig(final int id, final int componentId, final Block.Sector sector) {
		this.id = id;
		this.componentId = componentId;
		this.stream = new JagexBufferStream(sector.getPayload());
		read();
	}

	public static CacheComponentConfig[] load(final AbstractCacheWorker worker, final int widgetId) {
		if (CACHE.containsKey(widgetId)) {
			return CACHE.get(widgetId);
		}

		final Block b = worker.getBlock(CACHE_IDX, widgetId);
		if (b == null) {
			CACHE.put(widgetId, new CacheComponentConfig[]{});
			return null;
		}
		final CacheComponentConfig[] comps = new CacheComponentConfig[b.getSectors().length];
		final Block.Sector[] sectors = b.getSectors();
		for (int i = 0; i < sectors.length; i++) {
			final int componentId = (widgetId << 16) + i;
			comps[i] = new CacheComponentConfig(i, componentId, sectors[i]);
		}

		CACHE.put(widgetId, comps);
		return comps;
	}

	private void read() {
		if (this.stream.peek() == -1) {
			loadIf3();
		} else {
			loadIf1();
		}
	}

	private void loadIf3() {
		stream.getUByte();
		type = stream.getUByte();
		contentType = stream.getUShort();
		originalX = stream.getShort();
		originalY = stream.getShort();
		originalWidth = stream.getUShort();
		if (type == 9) {
			originalHeight = stream.getShort();
		} else {
			originalHeight = stream.getUShort();
		}

		widthMode = stream.getByte();
		heightMode = stream.getByte();
		xPositionMode = stream.getByte();
		yPositionMode = stream.getByte();
		parentId = stream.getUShort();
		if (parentId == 0xFFFF) {
			parentId = -1;
		} else {
			parentId += id & ~0xFFFF;
		}

		isHidden = stream.getUByte() == 1;
		if (type == 0) {
			scrollWidth = stream.getUShort();
			scrollHeight = stream.getUShort();
			noClickThrough = stream.getUByte() == 1;
		}

		if (type == 5) {
			spriteId = stream.getInt();
			textureId = stream.getUShort();
			spriteTiling = stream.getUByte() == 1;
			opacity = stream.getUByte();
			borderType = stream.getUByte();
			shadowColor = stream.getInt();
			flippedY = stream.getUByte() == 1;
			flippedX = stream.getUByte() == 1;
		}

		if (type == 6) {
			modelType = 1;
			modelId = stream.getUShort();
			if (modelId == 0xFFFF) {
				modelId = -1;
			}

			offsetX2d = stream.getShort();
			offsetY2d = stream.getShort();
			rotationX = stream.getUShort();
			rotationZ = stream.getUShort();
			rotationY = stream.getUShort();
			modelZoom = stream.getUShort();
			animation = stream.getUShort();
			if (animation == 0xFFFF) {
				animation = -1;
			}

			orthogonal = stream.getUByte() == 1;
			stream.getUShort();
			if (widthMode != 0) {
				modelHeightOverride = stream.getUShort();
			}

			if (heightMode != 0) {
				stream.getUShort();
			}
		}

		if (type == 4) {
			fontId = stream.getUShort();
			if (fontId == 0xFFFF) {
				fontId = -1;
			}

			text = stream.getString();
			lineHeight = stream.getUByte();
			xTextAlignment = stream.getUByte();
			yTextAlignment = stream.getUByte();
			textShadowed = stream.getUByte() == 1;
			textColor = stream.getInt();
		}

		if (type == 3) {
			textColor = stream.getInt();
			filled = stream.getUByte() == 1;
			opacity = stream.getUByte();
		}

		if (type == 9) {
			lineWidth = stream.getUByte();
			textColor = stream.getInt();
			lineDirection = stream.getUByte() == 1;
		}

		clickMask = (stream.getUByte() << 16) + (stream.getUByte() << 8) + stream.getUByte();
		name = stream.getString();
		final int var2 = stream.getUByte();
		if (var2 > 0) {
			actions = new String[var2];
			for (int var3 = 0; var3 < var2; ++var3) {
				actions[var3] = stream.getString();
			}
		}

		dragDeadZone = stream.getUByte();
		dragDeadTime = stream.getUByte();
		dragRenderBehavior = stream.getUByte() == 1;
		targetVerb = stream.getString();
		for (int i = 0; i < 18; i++) {
			skipListener();
		}
		for (int i = 0; i < 3; i++) {
			skipTriggers();
		}
	}

	private void loadIf1() {
		type = stream.getUByte();
		menuType = stream.getUByte();
		contentType = stream.getUShort();
		originalX = stream.getShort();
		originalY = stream.getShort();
		originalWidth = stream.getUShort();
		originalHeight = stream.getUShort();
		opacity = stream.getUByte();
		parentId = stream.getUShort();
		if (parentId == 0xFFFF) {
			parentId = -1;
		} else {
			parentId += id & ~0xFFFF;
		}

		hoveredSiblingId = stream.getUShort();
		if (hoveredSiblingId == 0xFFFF) {
			hoveredSiblingId = -1;
		}

		final int var2 = stream.getUByte();
		int var3;
		if (var2 > 0) {
			alternateOperators = new int[var2];
			alternateRhs = new int[var2];

			for (var3 = 0; var3 < var2; ++var3) {
				alternateOperators[var3] = stream.getUByte();
				alternateRhs[var3] = stream.getUShort();
			}
		}

		var3 = stream.getUByte();
		int var4;
		int var5;
		int var6;
		if (var3 > 0) {
			x = new int[var3][];
			for (var4 = 0; var4 < var3; ++var4) {
				var5 = stream.getUShort();
				x[var4] = new int[var5];

				for (var6 = 0; var6 < var5; ++var6) {
					x[var4][var6] = stream.getUShort();
					if (x[var4][var6] == '\uffff') {
						x[var4][var6] = -1;
					}
				}
			}
		}

		if (type == 0) {
			scrollHeight = stream.getUShort();
			isHidden = stream.getUByte() == 1;
		}

		if (type == 1) {
			stream.getUShort();
			stream.getUByte();
		}

		if (type == 2) {
			itemIds = new int[originalWidth * originalHeight];
			itemQuantities = new int[originalHeight * originalWidth];
			var4 = stream.getUByte();
			if (var4 == 1) {
				clickMask |= 268435456;
			}

			var5 = stream.getUByte();
			if (var5 == 1) {
				clickMask |= 1073741824;
			}

			var6 = stream.getUByte();
			if (var6 == 1) {
				clickMask |= Integer.MIN_VALUE;
			}

			final int var7 = stream.getUByte();
			if (var7 == 1) {
				clickMask |= 536870912;
			}

			xPitch = stream.getUByte();
			yPitch = stream.getUByte();
			spriteXOffsets = new int[20];
			spriteYOffsets = new int[20];
			sprites = new int[20];

			int var8;
			for (var8 = 0; var8 < 20; ++var8) {
				final int var9 = stream.getUByte();
				if (var9 == 1) {
					spriteXOffsets[var8] = stream.getShort();
					spriteYOffsets[var8] = stream.getShort();
					sprites[var8] = stream.getInt();
				} else {
					sprites[var8] = -1;
				}
			}

			configActions();
		}

		if (type == 3) {
			filled = stream.getUByte() == 1;
		}

		if (type == 4 || type == 1) {
			xTextAlignment = stream.getUByte();
			yTextAlignment = stream.getUByte();
			lineHeight = stream.getUByte();
			fontId = stream.getUShort();
			if (fontId == 0xFFFF) {
				fontId = -1;
			}

			textShadowed = stream.getUByte() == 1;
		}

		if (type == 4) {
			text = stream.getString();
			alternateText = stream.getString();
		}

		if (type == 1 || type == 3 || type == 4) {
			textColor = stream.getInt();
		}

		if (type == 3 || type == 4) {
			alternateTextColor = stream.getInt();
			mouseOverTextColor = stream.getInt();
			mouseOverTextColor2 = stream.getInt();
		}

		if (type == 5) {
			spriteId = stream.getInt();
			alternateSpriteId = stream.getInt();
		}

		if (type == 6) {
			modelType = 1;
			modelId = stream.getUShort();
			if (modelId == 0xFFFF) {
				modelId = -1;
			}

			alternateModelId = stream.getUShort();
			if (alternateModelId == 0xFFFF) {
				alternateModelId = -1;
			}

			animation = stream.getUShort();
			if (animation == 0xFFFF) {
				animation = -1;
			}

			alternateAnimation = stream.getUShort();
			if (alternateAnimation == 0xFFFF) {
				alternateAnimation = -1;
			}

			modelZoom = stream.getUShort();
			rotationX = stream.getUShort();
			rotationZ = stream.getUShort();
		}

		if (type == 7) {
			itemIds = new int[originalWidth * originalHeight];
			itemQuantities = new int[originalWidth * originalHeight];
			xTextAlignment = stream.getUByte();
			fontId = stream.getUShort();
			if (fontId == 0xFFFF) {
				fontId = -1;
			}

			textShadowed = stream.getUByte() == 1;
			textColor = stream.getInt();
			xPitch = stream.getShort();
			yPitch = stream.getShort();
			var4 = stream.getUByte();
			if (var4 == 1) {
				clickMask |= 1073741824;
			}

			configActions();
		}

		if (type == 8) {
			text = stream.getString();
		}

		if (menuType == 2 || type == 2) {
			targetVerb = stream.getString();
			spellName = stream.getString();
			var4 = stream.getUShort() & 63;
			clickMask |= var4 << 11;
		}

		if (menuType == 1 || menuType == 4 || menuType == 5 || menuType == 6) {
			tooltip = stream.getString();
			if (tooltip.length() == 0) {
				if (menuType == 1) {
					tooltip = "Ok";
				}
				if (menuType == 4) {
					tooltip = "Select";
				}
				if (menuType == 5) {
					tooltip = "Select";
				}
				if (menuType == 6) {
					tooltip = "Continue";
				}
			}
		}

		if (menuType == 1 || menuType == 4 || menuType == 5) {
			clickMask |= 4194304;
		}

		if (menuType == 6) {
			clickMask |= 1;
		}
	}

	private void configActions() {
		int var5;
		configActions = new String[5];
		for (var5 = 0; var5 < 5; ++var5) {
			final String var10 = stream.getString();
			if (var10.length() > 0) {
				configActions[var5] = var10;
				clickMask |= 1 << var5 + 23;
			}
		}
	}

	private void skipListener() {
		final int var2 = stream.getUByte();
		if (var2 != 0) {
			final Object[] var3 = new Object[var2];
			for (int var4 = 0; var4 < var2; ++var4) {
				final int var5 = stream.getUByte();
				if (var5 == 0) {
					var3[var4] = stream.getInt();
				} else if (var5 == 1) {
					var3[var4] = stream.getString();
				}
			}
		}
	}

	private void skipTriggers() {
		final int var2 = stream.getUByte();
		if (var2 != 0) {
			final int[] var3 = new int[var2];
			for (int var4 = 0; var4 < var2; ++var4) {
				var3[var4] = stream.getInt();
			}
		}
	}
}
