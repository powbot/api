package org.powerbot.bot;

public enum EventType {
	MESSAGE_EVENT, MODEL_RENDER_EVENT, PAINT_EVENT, TEXT_PAINT_EVENT,
	MOUSE_EVENT, MOUSE_MOTION_EVENT, MOUSE_WHEEL_EVENT, FOCUS_EVENT,
	KEY_EVENT, WIDGET_POSITION_EVENT;

	public int id() {
		return ordinal();
	}
}
