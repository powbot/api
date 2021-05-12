package org.powbot.walking.local.nodes

import org.powerbot.script.Condition
import org.powerbot.script.Locatable
import org.powerbot.script.Tile
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Constants
import org.powerbot.script.rt4.GameObject
import java.util.*


class LocalDoorEdge(val door: GameObject, parent: LocalEdge, destination: Tile) :
    LocalTileEdge(parent, destination) {

    override val type: LocalEdgeType =
        LocalEdgeType.DOOR

    override fun execute(): Boolean {
        return if (interact()) {
            val openedDoor = Condition.wait { openedDoor() }
//            println("Opened door: $openedDoor")
            openedDoor
        } else {
            false
        }
    }

    /**
     * Interact with the door
     */
    fun interact(): Boolean {
        val t = door
        val pos = (t as Locatable).tile()
        val ctx = ClientContext.ctx()
        val destination = ctx.movement.destination()
        if (!t.inViewport()
            || (destination != pos && pos.distanceTo(if (destination == Tile.NIL) ctx.players.local() else destination) > 12)
        ) {
            ctx.movement.step(pos)
//            ctx.camera.turnTo(pos)
        }
//        if (selectItem > -1) {
//            val item = ctx.inventory.firstOrNull { it.id() == selectItem } ?: return false
//            item.click()
//        }
        return t.interact(getAction())
    }

    /**
     * Return first action from possible 'opening' actions that is present in the [door]
     */
    fun getAction(): String? {
        return door.actions().firstOrNull { a -> a in actions }
    }

    /**
     * Returns true if [door] is opened (no longer exists)
     */
    fun openedDoor(): Boolean {
        return ClientContext.ctx().objects.toStream().at(door)
            .noneMatch { it.isDoor() && it.orientation() == door.orientation() }
    }

    override fun toString(): String {
        return "LocalDoorEdge(door=(${door.name()}) at=${door.tile()})"
    }

    companion object {
        val actions = listOf("Open", "Use", "Enter", "Pay")
        val doorNames = listOf(
            "Door",
            "Glass door",
            "Gate",
            "Large door",
            "Castle door",
            "Gate of War",
            "Rickety door",
            "Oozing barrier",
            "Portal of Death",
            "Magic guild door",
            "Prison door",
            "Barbarian door"
        )

        val blockedDoorTiles =
            listOf(Tile(2515, 9575, 0), Tile(2568, 9893, 0), Tile(2566, 9901, 0), Tile(2924, 9803, 0))

        /**
         * Returns true if the GameObject is not behind unobtainable requirements or blocked in general
         */
        fun GameObject.canUseDoor(): Boolean {
            val t = tile()
            if (t == Tile(2611, 3394, 0)) {
                return org.powerbot.script.ClientContext.ctx().skills.level(Constants.SKILLS_ATTACK) >= 68
            }
            return !blockedDoorTiles.contains(t)
        }

        /**
         * Returns a door for the given [orientation] if present.
         */
        fun Tile.getDoor(orientation: Int): Optional<GameObject> {
            return org.powerbot.script.ClientContext.ctx().objects.toStream().at(this).filter {
                it.name() != null && it.isDoor()
                    && it.orientation() == orientation
                    && it.canUseDoor()
            }.findFirst()
        }

        fun GameObject.isDoor(): Boolean {
            return doorNames.contains(name())
        }
    }
}
