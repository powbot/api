package org.powerbot.bot.cache

import org.powerbot.bot.rt4.client.internal.IWidget
import org.powerbot.script.WidgetPositionListener
import java.awt.Point

class WidgetPositionCache() : TimedCache<Int, Point>(), WidgetPositionListener {

    override fun onWidgetPosition(widget: IWidget?, x: Int, y: Int) {
        if (widget != null) {
            add(widget.hashCode(), Point(x, y))
        }
    }

    override fun load(id: Int): Point? {
        return null
    }
}
