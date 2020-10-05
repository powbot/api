package org.powerbot.bot.rt4.client.internal;

public interface IWidget extends INode {

	String[] getActions();

	int getAngleX();

	int getAngleY();

	int getAngleZ();

	int getBorderThickness();

	int getBoundsIndex();

	IWidget[] getChildren();

	int getContentType();

	int getHeight();

	int getId();

	int getItemId();

	int[] getItemIds();

	int getItemStackSize();

	int[] getItemStackSizes();

	int getModelId();

	int getModelType();

	int getModelZoom();

	int getParentId();

	int getScrollHeight();

	int getScrollWidth();

	int getScrollX();

	int getScrollY();

	String getText();

	int getTextColor();

	int getTextureId();

	String getTooltip();

	int getType();

	int getWidth();

	int getX();

	int getY();

	boolean isHidden();

}