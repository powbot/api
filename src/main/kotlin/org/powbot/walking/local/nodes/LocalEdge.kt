package org.powbot.walking.local.nodes

import org.powbot.walking.local.LocalPathFinder.getLocalNeighbors
import org.powerbot.script.ClientContext
import org.powerbot.script.Tile
import kotlin.math.abs

fun Tile.distanceM(dest: Tile): Int {
    if (floor() != dest.floor()) {
        return Int.MAX_VALUE
    }
    return abs(dest.x() - x()) + abs(dest.y() - y())
}

enum class LocalEdgeType {
    WALKING, DOOR
}


abstract class LocalEdge(val destination: Tile, val finalDestination: Tile) {

    abstract val type: LocalEdgeType

    abstract val parent: LocalEdge

    val heuristics: Double = destination.distanceM(finalDestination).toDouble()

    abstract fun getCost(): Double

    abstract fun getNeighbors(): MutableList<LocalEdge>

    abstract fun execute(): Boolean

    fun getPathCost(): Double {
        var cost = this.getCost()
        var curr = this
        while (curr.parent !is StartEdge) {
            cost += curr.parent.getCost()
            curr = curr.parent
        }
        return cost
    }

    override fun toString(): String {
        return "LocalEdge(to=${destination})"
    }
}

class StartEdge(startTile: Tile, finalDestination: Tile) : LocalEdge(startTile, finalDestination) {
    override val type: LocalEdgeType =
        LocalEdgeType.WALKING

    override val parent: LocalEdge = this

    override fun getCost(): Double = 1.0

    override fun getNeighbors(): MutableList<LocalEdge> {
        return this.getLocalNeighbors()
    }

    override fun execute(): Boolean {
        return ClientContext.ctx().movement.step(destination)
    }

    override fun toString(): String {
        return "StartEdge(to=${destination})"
    }
}
