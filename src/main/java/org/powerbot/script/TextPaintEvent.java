package org.powerbot.script;

import org.powerbot.bot.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.EventListener;

/**
 * TextPaintEvent
 * An event that is dispatched when the game requests the graphic buffer.
 */
public class TextPaintEvent extends AbstractEvent {
	public static final int EVENT_ID = EventType.TEXT_PAINT_EVENT.id();
	private static final long serialVersionUID = 7174559879186449999L;
	public Graphics graphics;
	public int index = 0;

	public TextPaintEvent() {
		super(EVENT_ID);
	}

	public TextPaintEvent(final Graphics graphics) {
		super(EVENT_ID);
		this.graphics = graphics;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void call(final EventListener eventListener) {
		if (graphics == null) {
			try {
				((PaintListener) eventListener).repaint(null);
			} catch (final NullPointerException ignored) {
			}
			return;
		}
		final Graphics2D graphics2D = (Graphics2D) graphics;

		final Color s_background = graphics2D.getBackground();
		final Shape s_clip = graphics2D.getClip();
		final Color s_color = graphics2D.getColor();
		final Composite s_composite = graphics2D.getComposite();
		final Font s_font = graphics2D.getFont();
		final Paint s_paint = graphics2D.getPaint();
		final RenderingHints s_renderingHints = graphics2D.getRenderingHints();
		final Stroke s_stroke = graphics2D.getStroke();
		final AffineTransform s_transform = graphics2D.getTransform();

		index = ((TextPaintListener) eventListener).draw(index, graphics);

		graphics2D.setBackground(s_background);
		graphics2D.setClip(s_clip);
		graphics2D.setColor(s_color);
		graphics2D.setComposite(s_composite);
		graphics2D.setFont(s_font);
		graphics2D.setPaint(s_paint);
		graphics2D.setRenderingHints(s_renderingHints);
		graphics2D.setStroke(s_stroke);
		graphics2D.setTransform(s_transform);
	}
}
