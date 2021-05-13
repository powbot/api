package org.powbot.walking.local.nodes

import org.powbot.walking.local.LocalPathFinder.getLocalNeighbors
import org.powerbot.script.Tile
import org.powerbot.script.rt4.ClientContext


open class LocalTileEdge(
    override val parent: LocalEdge,
    destination: Tile
) :
    LocalEdge(destination, parent.finalDestination) {

    override val type: LocalEdgeType =
        LocalEdgeType.WALKING

    override fun getCost(): Double {
        if (destination == finalDestination) {
            return 0.0
        }
        return 1.0
    }

    override fun getNeighbors(): MutableList<LocalEdge> {
        return getLocalNeighbors()
    }

    override fun execute(): Boolean {
        return ClientContext.ctx().movement.step(destination)
    }

    override fun toString(): String {
        return "LocalTileEdge(at=$destination)"
    }

}
