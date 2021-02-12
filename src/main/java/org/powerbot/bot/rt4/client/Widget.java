package org.powerbot.bot.rt4.client;

import org.powerbot.bot.*;
import org.powerbot.bot.rt4.client.internal.IWidget;

public class Widget extends Proxy<IWidget> {

	public Widget(final IWidget wrapped) {
		super(wrapped);
	}

	public int getX() {
		if (!isNull()) {
			return get().getX();
		}

		return -1;
	}

	public int getY() {
		if (!isNull()) {
			return get().getY();
		}

		return -1;
	}

	public int getWidth() {
		if (!isNull()) {
			return get().getWidth();
		}

		return -1;
	}

	public int getHeight() {
		if (!isNull()) {
			return get().getHeight();
		}

		return -1;
	}

	public int getBorderThickness() {
		if (!isNull()) {
			return get().getBorderThickness();
		}

		return -1;
	}

	public int getType() {
		if (!isNull()) {
			return get().getType();
		}

		return -1;
	}

	public int getId() {
		if (!isNull()) {
			return get().getId();
		}

		return -1;
	}

	public int getParentId() {
		if (!isNull()) {
			return get().getParentId();
		}

		return -1;
	}

	public Widget[] getChildren() {
		if (!isNull()) {
			final IWidget[] widgets = get().getChildren();
			final Widget[] wrapped = widgets != null ? new Widget[widgets.length] : null;
			if (widgets != null) {
				for (int i = 0; i < widgets.length; i++) {
					wrapped[i] = new Widget(widgets[i]);
				}
			}

			return wrapped;
		}

		return null;
	}

	public int getContentType() {
		if (!isNull()) {
			return get().getContentType();
		}

		return -1;
	}

	public int getModelId() {
		if (!isNull()) {
			return get().getModelId();
		}

		return -1;
	}

	public int getModelType() {
		if (!isNull()) {
			return get().getModelType();
		}

		return -1;
	}

	public int getModelZoom() {
		if (!isNull()) {
			return get().getModelZoom();
		}

		return -1;
	}

	public String[] getActions() {
		if (!isNull()) {
			return get().getActions();
		}

		return null;
	}

	public int getAngleX() {
		if (!isNull()) {
			return get().getAngleX();
		}

		return -1;
	}

	public int getAngleY() {
		if (!isNull()) {
			return get().getAngleY();
		}

		return -1;
	}

	public int getAngleZ() {
		if (!isNull()) {
			return get().getAngleZ();
		}

		return -1;
	}

	public String getText() {
		if (!isNull()) {
			return get().getText();
		}

		return null;
	}

	public int getTextColor() {
		if (!isNull()) {
			return get().getTextColor();
		}

		return -1;
	}

	public int getScrollX() {
		if (!isNull()) {
			return get().getScrollX();
		}

		return -1;
	}

	public int getScrollY() {
		if (!isNull()) {
			return get().getScrollY();
		}

		return -1;
	}

	public int getScrollWidth() {
		if (!isNull()) {
			return get().getScrollWidth();
		}

		return -1;
	}

	public int getScrollHeight() {
		if (!isNull()) {
			return get().getScrollHeight();
		}

		return -1;
	}

	public int getBoundsIndex() {
		if (!isNull()) {
			return get().getBoundsIndex();
		}

		return -1;
	}

	public int getTextureId() {
		if (!isNull()) {
			return get().getTextureId();
		}

		return -1;
	}

	public int[] getItemIds() {
		if (!isNull()) {
			return get().getItemIds();
		}

		return null;
	}

	public int[] getItemStackSizes() {
		if (!isNull()) {
			return get().getItemStackSizes();
		}

		return null;
	}

	public boolean isHidden() {
		if (!isNull()) {
			return get().isHidden();
		}

		return false;
	}

	public String getTooltip() {
		if (!isNull()) {
			return get().getTooltip();
		}

		return null;
	}

	public int getItemId() {
		if (!isNull()) {
			return get().getItemId();
		}

		return -1;
	}

	public int getItemStackSize() {
		if (!isNull()) {
			return get().getItemStackSize();
		}

		return -1;
	}
}
