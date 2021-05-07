package org.powbot.walking.local

import org.powbot.walking.local.Flag.Rotation
import org.powbot.walking.local.nodes.LocalDoorEdge
import org.powbot.walking.local.nodes.LocalDoorEdge.Companion.getDoor
import org.powbot.walking.local.nodes.LocalEdge
import org.powbot.walking.local.nodes.LocalTileEdge
import org.powbot.walking.local.nodes.StartEdge
import org.powerbot.bot.rt4.client.internal.ICollisionMap
import org.powerbot.script.ClientContext
import org.powerbot.script.Locatable
import org.powerbot.script.Tile
import org.powerbot.script.rt4.GameObject
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger


object LocalPathFinder {

    val maxAttempts = 2500
    lateinit var cachedFlags: ICollisionMap
    val logger = Logger.getLogger("LocalPathFinder")

    fun findPath(end: Tile?): LocalPath {
        return if (end != null) {
            findPath(ClientContext.ctx().players.local().tile(), end)
        } else {
            LocalPath(emptyList())
        }
    }

    fun findPath(begin: Tile, end: Tile): LocalPath {
        this.cachedFlags = ClientContext.ctx().client().collisionMaps[begin.floor()]
        val end = if (end.blocked(cachedFlags)) end.getWalkableNeighbor() else end

        val startAction = StartEdge(
            begin,
            end ?: begin
        )
        if (begin == end || end == null) {
            return LocalPath(listOf(startAction))
        }
        logger.log(Level.INFO, "FIND LOCAL PATH: $begin -> $end, distance: ${begin.distanceTo(end)}")
        var attempts = 0

        val open = startAction.getLocalNeighbors(end)
        val searched = mutableListOf<LocalEdge>()

        while (open.isNotEmpty() && attempts < maxAttempts) {
            val next = getBest(open) ?: continue
            open.remove(next)
//            println("${next.javaClass.simpleName}: ${next.source} -> ${next.destination}")

            if (next.destination == end) {
                return buildPath(next)
            }
            val neighbors = next.getNeighbors().filterNot {
                open.contains(it.destination) || searched.contains(it.destination)
            }
//            println("Found ${neighbors.size} neighbors")
            open.addAll(neighbors)

            searched.add(next)
            attempts++
        }
        logger.log(Level.WARNING, "Ran out of attempts trying to get LOCAL path to: $end")

        return LocalPath(emptyList())
    }

    fun buildPath(last: LocalEdge): LocalPath {
        val path = LinkedList<LocalEdge>()
        var r = last
        while (r !is StartEdge) {
            path.add(r)
            r = r.parent
        }
        return LocalPath(path.reversed())
    }

    fun getBest(open: Collection<LocalEdge>): LocalEdge? {
        return open.minByOrNull { it.getPathCost() + it.heuristics }
    }

    fun MutableList<LocalEdge>.contains(destination: Tile): Boolean {
        return any { it.destination == destination }
    }

    /**
     * Used to find neighbors of LocalTile
     */
    fun LocalEdge.getLocalNeighbors(
        finalDesination: Tile,
        flags: ICollisionMap = cachedFlags
    ): MutableList<LocalEdge> {
        var neighbors = mutableListOf<LocalEdge>()

        val current = destination
        val p = current.floor()

        val n = Tile(current.x(), current.y() + 1, p)
        val e = Tile(current.x() + 1, current.y(), p)
        val s = Tile(current.x(), current.y() - 1, p)
        val w = Tile(current.x() - 1, current.y(), p)

        val ne = Tile(current.x() + 1, current.y() + 1, p)
        val se = Tile(current.x() + 1, current.y() - 1, p)
        val sw = Tile(current.x() - 1, current.y() - 1, p)
        val nw = Tile(current.x() - 1, current.y() + 1, p)

        if (!n.blocked(flags)) {
            if (!current.blocked(flags, Flag.W_N)) {
                neighbors.add(
                    LocalTileEdge(
                        this,
                        n
                    )
                )
            } else {
                var door = current.getDoor(Rotation.NORTH)
                if (door.isEmpty) door = n.getDoor(Rotation.SOUTH)
                if (door.isPresent) {
                    neighbors.add(
                        LocalDoorEdge(
                            door.get(),
                            this,
                            n
                        )
                    )
                }
            }
        }
        if (!e.blocked(flags)) {
            if (!current.blocked(flags, Flag.W_E)) {
                neighbors.add(
                    LocalTileEdge(
                        this,
                        e
                    )
                )
            } else {
                var door = current.getDoor(Rotation.EAST)
                if (door.isEmpty) door = e.getDoor(Rotation.WEST)
                if (door.isPresent) {
                    neighbors.add(
                        LocalDoorEdge(
                            door.get(),
                            this,
                            e
                        )
                    )
                }
            }
        }
        if (!s.blocked(flags)) {
            if (!current.blocked(flags, Flag.W_S)) {
                neighbors.add(
                    LocalTileEdge(
                        this,
                        s
                    )
                )
            } else {
                var door = current.getDoor(Rotation.SOUTH)
                if (door.isEmpty) door = s.getDoor(Rotation.NORTH)
                if (door.isPresent) {
                    neighbors.add(
                        LocalDoorEdge(
                            door.get(),
                            this,
                            s
                        )
                    )
                }
            }
        }
        if (!w.blocked(flags)) {
            if (!current.blocked(flags, Flag.W_W)) {
                neighbors.add(
                    LocalTileEdge(
                        this,
                        w
                    )
                )
            } else {
                var door = current.getDoor(Rotation.WEST)
                if (door.isEmpty) door = w.getDoor(Rotation.EAST)
                if (door.isPresent) {
                    neighbors.add(
                        LocalDoorEdge(
                            door.get(),
                            this,
                            w
                        )
                    )
                }
            }
        }

        if (!current.blocked(flags, Flag.W_NE or Flag.W_N or Flag.W_E)
            && !n.blocked(flags, Flag.W_E)
            && !e.blocked(flags, Flag.W_N)
        ) {
            neighbors = checkForDiagonal(ne, neighbors)
        }
        if (!current.blocked(flags, Flag.W_SE or Flag.W_S or Flag.W_E)
            && !s.blocked(flags, Flag.W_E)
            && !e.blocked(flags, Flag.W_S)
        ) {
            neighbors = checkForDiagonal(se, neighbors)
        }
        if (!current.blocked(flags, Flag.W_SW or Flag.W_S or Flag.W_W)
            && !s.blocked(flags, Flag.W_W)
            && !w.blocked(flags, Flag.W_S)
        ) {
            neighbors = checkForDiagonal(sw, neighbors)
        }
        if (!current.blocked(flags, Flag.W_NW or Flag.W_N or Flag.W_W)
            && !n.blocked(flags, Flag.W_W)
            && !w.blocked(flags, Flag.W_N)
        ) {
            neighbors = checkForDiagonal(nw, neighbors)
        }

        return neighbors
    }

    /**
     * If the given tile (diagonal neighbor) is not blocked, it gets added to the neighbors.
     * If it is blocked, we check if there is a door that has a diagonal rotation [Flag.Rotation.DIAGONAL]
     */
    fun LocalEdge.checkForDiagonal(
        tile: Tile,
        neighbors: MutableList<LocalEdge>,
        flags: ICollisionMap = cachedFlags
    ): MutableList<LocalEdge> {
        if (!tile.blocked(flags)) {
            neighbors.add(
                LocalTileEdge(
                    this,
                    tile
                )
            )
        } else {
            val door = tile.getDoor(Rotation.DIAGONAL)
            if (door.isPresent) {
                neighbors.add(
                    LocalDoorEdge(
                        door.get(),
                        this,
                        tile
                    )
                )
            }
        }
        return neighbors
    }

    /**
     * Gets all neighboring tiles to the [Locatable] and checks which ones can be walked on
     * @return walkable neighboring tile, nearest to the local player
     */
    fun Locatable.getWalkableNeighbor(
        diagonalTiles: Boolean = false,
        filter: (Tile) -> Boolean = { true }
    ): Tile? {
        val walkableNeighbors = getWalkableNeighbors(diagonalTiles)
        return walkableNeighbors.filter(filter).minByOrNull { it.distance() }
    }

    fun Locatable.getWalkableNeighbors(
        diagonalTiles: Boolean = false
    ): MutableList<Tile> {

        val t = tile()
        val x = t.x()
        val y = t.y()
        val f = t.floor()

        val n = Tile(x, y + 1, f)
        val e = Tile(x + 1, y, f)
        val s = Tile(x, y - 1, f)
        val w = Tile(x - 1, y, f)
        val straight = listOf(n, e, s, w)
        val ne = Tile(x + 1, y + 1, f)
        val se = Tile(x + 1, y - 1, f)
        val sw = Tile(x - 1, y - 1, f)
        val nw = Tile(x - 1, y + 1, f)
        val diagonal = listOf(ne, se, sw, nw)

        val walkableNeighbors = mutableListOf<Tile>()
        walkableNeighbors.addAll(straight.filter { !it.blocked(cachedFlags) })

        if (diagonalTiles) {
            walkableNeighbors.addAll(diagonal.filter { !it.blocked(cachedFlags) })
        }
        return walkableNeighbors
    }
}
