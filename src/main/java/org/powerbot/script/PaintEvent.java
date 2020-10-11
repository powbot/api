package org.powerbot.script;

import org.powerbot.bot.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.EventListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PaintEvent
 * An event that is dispatched when the game requests the graphic buffer.
 */
public class PaintEvent extends AbstractEvent {
	public static final int EVENT_ID = EventType.PAINT_EVENT.id();
	private static final long serialVersionUID = 4772234942045737667L;
	public Graphics graphics;

	public PaintEvent() {
		super(EVENT_ID);
	}

	public PaintEvent(final Graphics graphics) {
		super(EVENT_ID);
		this.graphics = graphics;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void call(final EventListener e) {
		if (graphics == null) {
			try {
				((PaintListener) e).repaint(null);
			} catch (final Exception ignored) {
			}
			return;
		}
		final Graphics2D g2 = (Graphics2D) graphics;

		final Color b = g2.getBackground();
		final Shape l = g2.getClip();
		final Color c = g2.getColor();
		final Composite m = g2.getComposite();
		final Font f = g2.getFont();
		final Paint p = g2.getPaint();
		final RenderingHints r = g2.getRenderingHints();
		final Stroke s = g2.getStroke();
		final AffineTransform t = g2.getTransform();

		try {
			((PaintListener) e).repaint(graphics);
		} catch (final Exception ex) {
			Logger.getAnonymousLogger().log(Level.WARNING, null, ex);
		}

		g2.setBackground(b);
		g2.setClip(l);
		g2.setColor(c);
		g2.setComposite(m);
		g2.setFont(f);
		g2.setPaint(p);
		g2.setRenderingHints(r);
		g2.setStroke(s);
		g2.setTransform(t);
	}
}
