package org.powbot.walking.obstacle

import org.powbot.walking.obstacle.ObstacleHandlerCamera.turnCameraOrientation
import org.powbot.walking.obstacle.ObstacleHandlerUtils.isChatting
import org.powbot.walking.obstacle.SOS.handleSOSDoors
import org.powbot.walking.obstacle.SOS.handleSOSWarning
import org.powbot.walking.obstacle.SOS.inSOS
import org.powerbot.script.Area
import org.powerbot.script.ClientContext.ctx
import org.powerbot.script.Condition
import org.powerbot.script.Random
import org.powerbot.script.Tile
import org.powerbot.script.rt4.*
import java.util.logging.Logger


object ObstacleHandler  {
    
    val log = Logger.getLogger("ObstacleHandler")

    val rGaussian = Random.nextGaussian(35, 100, 24.0)
    const val DramenStaff = 772
    const val Coins = 995
    const val CaveYValueThreshold = 4500
    const val SpiritTreeWidget = 187
    val ChatWidgets = intArrayOf(231, 219, 217)

    val ObjectNames = listOf(
        "door", "hole", "gate", "rope", "entrance", "cave", "rift", "dripping vine", "stairs",
        "stepping stone", "gate of war", "vines", "portal of death", "oozing barrier", "gangplank",
        "portal", "cave", "steps", "lift", "chasm", "staircase", "spirit tree", "shantay pass",
        "ladder", "trapdoor", "statue", "stile"
    )
    
    val SpiritTreeGnomeStronghold = Tile(2461, 3444, 0)

    val VaultOfWarArea: Area = Area(
        Tile(1851, 5252, 0),
        Tile(1924, 5252, 0),
        Tile(1935, 5226, 0),
        Tile(1911, 5180, 0),
        Tile(1851, 5179, 0)
    )
    val CatacombsOfFamineArea: Area = Area(
        Tile(2051, 5249, 0),
        Tile(1982, 5248, 0),
        Tile(1982, 5183, 0),
        Tile(2050, 5183, 0)
    )
    val PitOfPestilenceArea: Area = Area(
        Tile(2147, 5318, 0),
        Tile(2107, 5308, 0),
        Tile(2112, 5245, 0),
        Tile(2178, 5245, 0),
        Tile(2179, 5315, 0)
    )
    val SephulchreOfDeathArea: Area = Area(
        Tile(2332, 5250, 0),
        Tile(2300, 5247, 0),
        Tile(2303, 5179, 0),
        Tile(2375, 5182, 0),
        Tile(2370, 5250, 0)
    )
    val ZanarisLumbridgeHut = Area(
        Tile(3202, 3171, 0),
        Tile(3202, 3167, 0),
        Tile(3206, 3167, 0),
        Tile(3206, 3171, 0)
    )


    fun handleObstacle(t: Array<Tile>): Boolean {
        val ctx = ctx()
        val nextTile: Tile = getNextTile(t) ?: return false
        val nextReachable: Tile = getNextReachableTile(t, nextTile) ?: return false
        /*
         * Return false as there is no obstacle to handle.
         * Perhaps this was called whilst we were still walking
         * and the tile became reachable?
         */
        if (nextTile.matrix(ctx).reachable()) {
            return false
        }

        val p: Player = ctx.players.local()
        var distance = Double.POSITIVE_INFINITY
        var obstacle: GameObject? = null
        for (go: GameObject in ctx.objects.select(9).select { gameObject ->
            ObjectNames.any { gameObject.name().contains(it, true) }
        }
            .select { `object` -> `object`.tile().distanceTo(nextReachable) < 9 }.nearest(nextReachable)) {
            if (org.powerbot.script.ClientContext.ctx().controller.script() == null) {
                return false
            }
            val calcDist: Double = go.tile().distanceTo(Tile(nextTile.x(), nextTile.y(), p.tile().floor()))
            log.info(
                "Obstacle name: " + go.name().toString() +
                    " | Distance: " + calcDist.toString() + " | Orientation: " + go.orientation()
            )
            if (nextTile.floor() != p.tile().floor()) {
                if ((go.type() === GameObject.Type.INTERACTIVE
                        && go.actions().isNotEmpty())
                ) {
                    if (nextTile.floor() > p.tile().floor()) {
                        if (calcDist < distance && reachable(go)) {
                            for (s: String? in go.actions()) {
                                if (((s != null) && s != "null"
                                        && ((s.contains("Enter")
                                        || (s == "Climb") || (s == "Walk-up") || s.contains("Climb-up")
                                        || ((s.contains("Climb-down")
                                        && nextTile.y() > CaveYValueThreshold)))))
                                ) {
                                    obstacle = go
                                    distance =
                                        calcDist //Set the distance to the object so we can compare future objects against it to determine the best.
                                    break
                                }
                            }
                        }
                    } else {
                        if (calcDist < distance && reachable(go)) {
                            for (s: String? in go.actions()) {
                                if (((s != null) && s != "null"
                                        && ((s.contains("Enter")
                                        || s.contains("Cross")
                                        || s.contains("Climb-down"))))
                                ) {
                                    obstacle = go
                                    distance =
                                        calcDist //Set the distance to the object so we can compare future objects against it to determine the best.
                                    break
                                }
                            }
                        }
                    }
                }
            } else if ((nextTile.distanceTo(ctx.players.local()) > 25
                    || nextTile.y() > CaveYValueThreshold
                    && p.tile().y() < CaveYValueThreshold)
            ) {
                if (((((go.type() !== GameObject.Type.BOUNDARY
                        ) || go.name().toLowerCase().contains("rift")
                        || go.name().toLowerCase().contains("cave")
                        || go.name().equals("chasm", true)
                        || go.name().toLowerCase().contains("hole")))
                        && !go.name().equals("staircase", true))
                ) {
                    for (s: String? in go.actions()) {
                        if ((go.tile().distanceTo(nextTile) + go.tile().distanceTo(p) < distance
                                && reachable(go))
                        ) {
                            if (((s != null) && s != "null"
                                    && ((s.contains("Pay")
                                    || s.contains("Open")
                                    || s.contains("Travel")
                                    || s.contains("Investigate")
                                    || s.contains("Use")
                                    || s.contains("Enter")
                                    || s.contains("Climb-down")
                                    || s.contains("Climb-up"))))
                            ) {
                                obstacle = go
                                distance = go.tile()
                                    .distanceTo(nextTile) //Set the distance to the object so we can compare future objects against it to determine the best.
                                break
                            }
                        }
                    }
                }
            } else {
                if (((go.type() === GameObject.Type.BOUNDARY
                        ) || go.name().contains("door", true)
                        || go.name().equals("vines", true)
                        || go.name().equals("icy cavern", true)
                        || go.name().equals("stepping stone", true)
                        || ((go.name().equals("portal", true)
                        && !inSOS()
                        )) || go.name().equals("shantay pass", true))
                ) {
                    if (calcDist < distance && reachable(go)) {
                        obstacle = go
                        distance = calcDist //Set the distance to the object so we can compare future objects against it to determine the best.
                    }
                }
            }
        }
        if (obstacle == null) return false
        log.info("Next tile not reachable. Handling " + obstacle.name())

        if (obstacle.inViewport()) {
            if (nextTile.floor() > p.tile().floor()) {
                for (action: String? in obstacle.actions()) {
                    if (action != null) {
                        when (action) {
                            "Walk-up", "Enter", "Climb-up", "Climb", "Climb-down" -> return handleObstacle(
                                obstacle,
                                action,
                                nextTile
                            )
                        }
                    }
                }
            } else if (nextTile.floor() < p.tile().floor()) {
                for (action: String? in obstacle.actions()) {
                    if (action != null) {
                        when (action) {
                            "Cross", "Enter", "Climb-down" -> return handleObstacle(obstacle, action, nextTile)
                        }
                    }
                }
            } else if ((nextTile.distanceTo(ctx.players.local()) > 40
                    ) || (p.tile().y() >= CaveYValueThreshold
                    ) || ((nextTile.y() > CaveYValueThreshold
                    && p.tile().y() < CaveYValueThreshold))
            ) {
                for (action: String? in obstacle.actions()) {
                    if (action != null) {
                        when (action) {
                            "Jump-from" -> return if (ctx.skills.realLevel(Constants.SKILLS_AGILITY) >= 14) {
                                handleObstacle(obstacle, action, nextTile)
                            } else {
                                log.info(
                                    "Not high enough agility level."
                                )
                                false
                            }
                            "Pay", "Investigate", "Climb-down", "Climb-up", "Open", "Use", "Enter" -> return handleObstacle(
                                obstacle,
                                action,
                                nextTile
                            )
                            "Chop-down" -> return if ((!ctx.inventory.select()
                                    .select { item -> item.name().contains("axe") }
                                    .isEmpty
                                    && ctx.skills.realLevel(Constants.SKILLS_WOODCUTTING) >= 34)
                            ) {
                                handleObstacle(obstacle, action, nextTile)
                            } else {
                                log.info("No axe available.")
                                false
                            }
                            "Travel" -> return if (obstacle.interact(action)) {
                                handleSpiritTree(nextTile)
                            } else {
                                turnCameraOrientation(if (obstacle.orientation() > 4) obstacle.orientation() - 4 else obstacle.orientation() + 4)
                                false
                            }
                        }
                    }
                }
            } else {
                for (action: String? in obstacle.actions()) {
                    if (action != null) {
                        when (action) {
                            "Open", "Use" -> {
                                if (ZanarisLumbridgeHut.contains(nextTile)) {
                                    if (!(equipDramenStaff())) {
                                        break
                                    }
                                }
                                return handleObstacle(obstacle, action, nextTile)
                            }
                            "Climb-over", "Go-through" -> return handleObstacle(obstacle, action, nextTile)
                        }
                    }
                }
            }
        } else {
            if (ctx.movement.step(obstacle)) {
                Condition.wait({ p.animation() == -1 && !p.inMotion() }, 100, 35)
            }
            ctx.camera.turnTo(obstacle, Random.nextInt(40, 80))
        }
        return false
    }

    private fun handleObstacle(obstacle: GameObject, _action: String, nextTile: Tile): Boolean {
        val action = when (_action) {
            "Climb" ->
                if (nextTile.floor() > ClientContext.ctx().client().floor
                    && obstacle.actions()?.contains("Climb-up") == true) {
                    "Climb-up"
                } else if (nextTile.floor() < ClientContext.ctx().client().floor
                    && obstacle.actions()?.contains("Climb-down") == true) {
                    "Climb-down"
                } else {
                    _action
                }
            else -> _action
        }
        return if (obstacle.interact(action) || ctx().input.click(obstacle.basePoint(), true)) {
            handlePostInteraction(obstacle, nextTile)
        } else {
            turnCameraOrientation(if (obstacle.orientation() > 4) obstacle.orientation() - 4 else obstacle.orientation() + 4)
            false
        }
    }

    private fun handleSpiritTree(nextTile: Tile): Boolean {
        val ctx = ctx()
        val p: Player = ctx.players.local()
        if (ctx.game.crosshair() === Game.Crosshair.ACTION) {
            if (Condition.wait(this::spiritTreeMenuOpen)) {
                if (nextTile.distanceTo(SpiritTreeGnomeStronghold) < 10) {
                    ctx.input.send("2")
                    return Condition.wait {
                        SpiritTreeGnomeStronghold.distanceTo(
                            p
                        ) < 10
                    }
                }
            }
        }
        return false
    }

    private fun handlePostInteraction(`object`: GameObject, nextTile: Tile): Boolean {
        val ctx = ctx()
        if (ctx.game.crosshair() === Game.Crosshair.ACTION) {
            if (inSOS()) {
                if (`object`.name().equals("ladder", true)) {
                    Condition.wait({ nextTile.matrix(ctx).reachable() || handleSOSWarning() }, 30, 100)
                } else if (`object`.name().contains("Door")) {
                    handleSOSDoors()
                }
            } else if (`object`.name().equals("dungeon entrance", true)) {
                handleDungeonEntrance()
            }
            return waitPostInteraction(nextTile, `object`)
        } else {
            if (`object`.tile().distanceTo(ctx.players.local()) > 3) {
                ctx.movement.step(`object`)
            }
            turnCameraOrientation(if (`object`.orientation() > 4) `object`.orientation() - 4 else `object`.orientation() + 4)
        }
        return false
    }


    private fun spiritTreeMenuOpen(): Boolean {
        return ctx().widgets.widget(SpiritTreeWidget).valid()
    }

    private fun equipDramenStaff(): Boolean {
        val ctx = ctx()
        if (ctx.equipment.select().id(DramenStaff).isEmpty) {
            if (!ctx.inventory.select().id(DramenStaff).isEmpty) {
                if (ctx.game.tab() !== Game.Tab.INVENTORY) {
                    ctx.game.tab(Game.Tab.INVENTORY)
                }
                if (ctx.game.tab() === Game.Tab.INVENTORY) {
                    val dramenStaff: Item =
                        ctx.inventory.select().id(DramenStaff).poll()
                    if (dramenStaff.interact("Wield")) {
                        return Condition.wait {
                            !ctx.equipment.select().id(DramenStaff)
                                .isEmpty
                        }
                    }
                }
            }
        }
        return false
    }



    private fun handleDungeonEntrance(): Boolean {
        val ctx = ctx()
        if (isChatting()) {
            if (ctx.inventory.select().id(Coins).count(true) >= 875) {
                while (isChatting()) {
                    if (ctx.chat.canContinue()) {
                        ctx.input.send("{VK_SPACE}")
                    } else {
                        ctx.input.send("1")
                    }
                }
            } else {
                log.info(
                    "Less than 875 coins in inventory." +
                        " Need more to enter the dungeon."
                )
            }
        }
        return false
    }

    private fun waitPostInteraction(nextTile: Tile, `object`: GameObject): Boolean {
        val ctx = ctx()
        val p: Player = ctx.players.local()
        return Condition.wait({
            ((p.animation() == -1 && nextTile.matrix(ctx).reachable()
                && !p.inMotion())
                || (!`object`.valid()
                && `object` !== ctx.objects.nil()) || isChatting())
        }, 100, 20)
    }

    private fun getNextReachableTile(t: Array<Tile>, notReachable: Tile): Tile? {
        val ctx = ctx()
        val tilePath = t.toList()
        for (i in 1 until tilePath.size) {
            if (tilePath.indexOf(notReachable) >= i) {
                val tile = tilePath[tilePath.indexOf(notReachable) - i]
                if (tile.matrix(ctx).reachable()) {
                    return tile
                }
            }
        }
        return if (tilePath.indexOf(notReachable) - 1 >= 0) {
            tilePath[tilePath.indexOf(notReachable) - 1]
        } else {
            ctx.players.local().tile()
        }
    }

    private fun getNextTile(t: Array<Tile>): Tile? {
        val ctx = ctx()
        val nextTile: Tile = ctx.movement.newTilePath(*t)?.next() ?: return null //The next tile, as suggested by the RSBot api, this will be the next reachable tile.
        var index = 0 //The index at which the next tile (by our definition) is at. Default to 0 (start tile).
        val p: Player = ctx.players.local()

        /*
         * Loop through the path, backwards.
         * Find the intended next tiles index
         * then check if there is a better option.
         */for (i in t.indices.reversed()) {
            if (t[i] == nextTile) {                                    // This is the index at which the suggested next tile resides
                if (i + 1 <= t.size - 1 && nextTile.distanceTo(p) < 3) {  // If we're not at the end of the path and we're very close to the suggested next tile
                    index = i + 1 // then it's too close to bother with. We will try to go to the tile after instead.
                    break
                }
                index = i // Suggested next tile was the best option as it is not very close, and reachable.
                break
            } else if (t[i].distanceTo(p) < 6) {
                index =
                    i // if next closest tile is < 6 and it's not the next tile then we can assume the next tile is probably not correct, perhaps no reachable tile available...
                break
            }
        }
        return t[index]
    }

    private fun reachable(go: GameObject): Boolean {
        val ctx = ctx()
        val a = go.width()
        val t1 = Tile(go.tile().x() + a, go.tile().y(), go.tile().floor())
        val t2 = Tile(go.tile().x() - a, go.tile().y(), go.tile().floor())
        val t3 = Tile(go.tile().x(), go.tile().y() + a, go.tile().floor())
        val t4 = Tile(go.tile().x(), go.tile().y() - a, go.tile().floor())
        return (t1.matrix(ctx).reachable()
            || t2.matrix(ctx).reachable()
            || t3.matrix(ctx).reachable()
            || t4.matrix(ctx).reachable())
    }
}
