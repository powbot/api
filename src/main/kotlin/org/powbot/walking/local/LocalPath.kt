package org.powbot.walking.local

import org.powbot.walking.local.nodes.LocalDoorEdge
import org.powbot.walking.local.nodes.LocalEdge
import org.powbot.walking.local.nodes.LocalEdgeType
import org.powerbot.bot.rt4.client.internal.ICollisionMap
import org.powerbot.script.ClientContext
import org.powerbot.script.Tile
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.geom.Line2D
import java.util.logging.Logger

class LocalPath(val actions: List<LocalEdge>) {

    fun traverse(): Boolean = actions.traverse()

    fun getNextEdge(): LocalEdge? = actions.getNext()

    companion object {

        val logger = Logger.getLogger("LocalPath")

        private fun List<LocalEdge>.getNext(): LocalEdge? {
            val cutOff = indexOf(minByOrNull { it.destination.distance() })
            val remainder = subList(cutOff, size)
//        println("Cutting of at: $cutOff of $size total, ${remainder.size} left")
            val firstSpecial = remainder.firstOrNull { it.type != LocalEdgeType.WALKING }
            if (firstSpecial != null) {
                logger.info("Found special: $firstSpecial")
                return firstSpecial
            } else {
                val possibleOptions = remainder.filter { it.destination.distance() <= 13 }
//            possibleOptions.forEach { println(it.destination) }
                return possibleOptions.lastOrNull() ?: null
            }
        }

        private fun List<LocalEdge>.traverse(): Boolean {
            if (isEmpty()) {
                return true
            }
            val next = getNext() ?: return false
            return if (next.type != LocalEdgeType.WALKING) {
                if (next.destination.distance() > 5) {
                    next.parent.execute()
                } else {
                    next.execute()
                }
                false
            } else {
                next.execute()
            }
        }
    }

    fun isEmpty(): Boolean = actions.isEmpty()

    fun isNotEmpty(): Boolean = actions.isNotEmpty()

    fun containsSpecialNode(): Boolean {
        return actions.any { it.type != LocalEdgeType.WALKING }
    }

    fun finalDestination(): Tile = actions.last().destination

    fun draw(g: Graphics2D) = actions.draw(g)

    private fun List<LocalEdge>.draw(g: Graphics2D) {
        val ctx = ClientContext.ctx()
        val flags = ctx.client().collisionMaps[ctx.players.local().tile().floor()]
        forEach {
            if (it is LocalDoorEdge) {
                it.destination.drawOnScreen(g, null, Color.CYAN, null)
            } else {
                it.destination.drawCollisions(g, flags)
            }
        }
    }

    fun Tile.drawOnScreen(
        g: Graphics,
        text: String? = null,
        outlineColor: Color? = Color.GREEN,
        fillColor: Color? = null
    ) {
        val c = g.color
        val bounds = matrix(ClientContext.ctx()).bounds()
        if (bounds != null) {
            val p = bounds
            if (outlineColor != null) {
                g.color = outlineColor
                g.drawPolygon(p)
            }
            if (fillColor != null) {
                g.color = fillColor
                g.fillPolygon(p)
            }
            if (text != null)
                g.drawString(text, bounds.bounds.centerX.toInt(), bounds.bounds.centerY.toInt())
        }
        g.color = c
    }

    fun Tile.drawCollisions(g: Graphics2D, flags: ICollisionMap) {
        val bounds = matrix(ClientContext.ctx()).bounds() ?: return
        if (blocked(flags)) {
            drawOnScreen(g, null, Color.RED)
        } else {
            val flag = collisionFlag(flags)
            val south = Line2D.Double(
                Point(bounds.xpoints[0], bounds.ypoints[0]),
                Point(bounds.xpoints[1], bounds.ypoints[1])
            )
            val east = Line2D.Double(
                Point(bounds.xpoints[1], bounds.ypoints[1]),
                Point(bounds.xpoints[2], bounds.ypoints[2])
            )
            val north = Line2D.Double(
                Point(bounds.xpoints[2], bounds.ypoints[2]),
                Point(bounds.xpoints[3], bounds.ypoints[3])
            )
            val west = Line2D.Double(
                Point(bounds.xpoints[3], bounds.ypoints[3]),
                Point(bounds.xpoints[0], bounds.ypoints[0])
            )
            g.color = if (flag and Flag.W_S == 0) Color.GREEN else Color.RED
            g.draw(south)

            g.color = if (flag and Flag.W_E == 0) Color.GREEN else Color.RED
            g.draw(east)

            g.color = if (flag and Flag.W_N == 0) Color.GREEN else Color.RED
            g.draw(north)

            g.color = if (flag and Flag.W_W == 0) Color.GREEN else Color.RED
            g.draw(west)
        }
    }

}
