package org.powerbot.script

import java.util.*

interface GameTickListener: EventListener {

    /**
     * Call upon each game tick
     */
    fun onGameTick(evt: GameTickEvent)
}
